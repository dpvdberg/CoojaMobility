package gui.group;

import org.contikios.cooja.*;
import org.contikios.cooja.plugins.Visualizer;
import utils.MobilityMote;
import utils.MoteGroup;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@ClassDescription("Mobility group panel")
@PluginType(PluginType.SIM_PLUGIN)
public class MoteGroupPanel extends VisPlugin {
    private static MoteGroupPanel instance = null;
    private final Simulation simulation;

    public static MoteGroupPanel getInstance() {
        return instance;
    }

    public static void build(Simulation simulation) {
        if (instance != null) {
            return;
        }
        Cooja cooja = simulation.getCooja();
        cooja.registerPlugin(MoteGroupPanel.class);
        cooja.tryStartPlugin(MoteGroupPanel.class, cooja, simulation, null);

        MoteGroupPanel groupPanel = (MoteGroupPanel) cooja.getPlugin(MoteGroupPanel.class.getName());
        groupPanel.hide();

        instance = groupPanel;

        groupPanel.setSize(new Dimension(400, 300));
    }

    private List<MobilityMote> motes;
    private List<MoteGroup> groups = new ArrayList<>();
    Visualizer visualizer = null;
    private JPanel mainPanel;
    private JTree moteTree;
    private JButton btnCreateGroup;
    private JButton btnRandomize;
    private JButton btnRemoveGroup;
    private JButton btnAddMotes;
    private final List<MoteGroupUpdateListener> listeners = new ArrayList<>();

    private final MoteGroup initialGroup = new MoteGroup("Initial Group");

    public MoteGroupPanel(Simulation simulation, final Cooja cooja) {
        super("Mobility groups", cooja);
        this.simulation = simulation;

        try {
            visualizer = (Visualizer) simulation.getCooja().getPlugin(Visualizer.class.getName());
        } catch (Exception ignored) {
            System.out.println("Could not get visualizer for mote group dialog");
        }

        refreshMotes();

        Box mainBox = Box.createVerticalBox();
        mainBox.add(mainPanel);
        this.getContentPane().add(BorderLayout.CENTER, mainBox);

        // remove all previous InternalFrameListeners
        for (InternalFrameListener listener : listenerList.getListeners(InternalFrameListener.class)) {
            listenerList.remove(InternalFrameListener.class, listener);
        }

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                e.getInternalFrame().hide();
            }
        });

        updateUIGroups();

        btnCreateGroup.addActionListener(e -> {
            String groupName = JOptionPane.showInputDialog(
                    this,
                    "Insert group name:",
                    "Group name",
                    JOptionPane.PLAIN_MESSAGE);
            this.groups.add(new MoteGroup(groupName));

            updateUIGroups();
        });

        btnRemoveGroup.addActionListener(e -> {
            TreePath[] paths = moteTree.getSelectionPaths();

            if (paths == null) {
                return;
            }
            List<MoteGroup> deleteGroups = new ArrayList<>();

            for (TreePath path : paths) {
                Object selectedObject = path.getLastPathComponent();
                if (selectedObject instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode) selectedObject).getUserObject();
                    if (userObject instanceof MoteGroup) {
                        deleteGroups.add((MoteGroup) userObject);
                    } else {
                        JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Only select groups to delete.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Please select groups to delete.");
                    return;
                }
            }

            for (MoteGroup group : deleteGroups) {
                if (group.equals(initialGroup)) {
                    JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Cannot remove the initial group.");
                    continue;
                }
                groups.remove(group);
            }

            updateUIGroups();
        });

        btnRandomize.addActionListener(e -> {
            if (groups.size() <= 0) {
                return;
            }
            groups.forEach(MoteGroup::clear);

            List<MobilityMote> allMotes = new ArrayList<>(motes);
            Collections.shuffle(allMotes);

            LinkedList<MobilityMote> queue = new LinkedList<>(allMotes);

            int motesPerGroup = (int) Math.ceil(allMotes.size() / (double) groups.size());
            List<List<MobilityMote>> groupMotes = new ArrayList<>();
            groupMotes.add(new ArrayList<>());

            int groupIndex = 0;
            int motesInGroup = 0;
            while (!queue.isEmpty()) {
                if (motesInGroup >= motesPerGroup) {
                    groupMotes.add(new ArrayList<>());
                    groupIndex++;
                    motesInGroup = 0;
                }
                groupMotes.get(groupIndex).add(queue.remove());
                motesInGroup++;
            }

            assert groups.size() == groupMotes.size();

            for (int i = 0; i < groups.size(); i++) {
                groups.get(i).addAll(groupMotes.get(i));
            }

            updateUIGroups();
        });

        btnAddMotes.addActionListener(e -> {
            Set<Mote> selectedMotes = visualizer.getSelectedMotes();
            List<MobilityMote> selectedMobilityMotes = motes.stream().filter(m -> selectedMotes.contains(m.getMote())).collect(Collectors.toList());

            TreePath[] paths = moteTree.getSelectionPaths();
            if (paths == null) {
                return;
            }
            if (paths.length != 1) {
                JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Please select one group in the panel.");
                return;
            }

            MoteGroup adder;
            TreePath path = paths[0];
            Object selectedObject = path.getLastPathComponent();
            if (selectedObject instanceof DefaultMutableTreeNode) {
                Object userObject = ((DefaultMutableTreeNode) selectedObject).getUserObject();
                if (userObject instanceof MoteGroup) {
                    adder = (MoteGroup) userObject;
                } else {
                    JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Only select a group to add motes to.");
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(Cooja.getTopParentContainer(), "Please select group to add motes to.");
                return;
            }

            for (MoteGroup group : groups) {
                group.removeAll(selectedMobilityMotes);
            }

            adder.addAll(selectedMobilityMotes);

            updateUIGroups();
        });
    }

    private void setGroups(List<MoteGroup> groups) {
        this.groups = groups;
        updateUIGroups();
    }

    private Set<MoteGroup> expandedGroups() {
        List<TreePath> expandedDescendants = Collections.list(moteTree.getExpandedDescendants(new TreePath(moteTree.getModel().getRoot())));

        Set<MoteGroup> groups = new HashSet<>();
        for (TreePath path : expandedDescendants) {
            for (Object nodeObject : path.getPath()) {
                if (nodeObject instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode) nodeObject).getUserObject();
                    if (userObject instanceof MoteGroup) {
                        groups.add((MoteGroup) userObject);
                    }
                }
            }
        }

        return groups;
    }

    private void updateUIGroups() {
        // Find orphans and add to initial group
        Set<MobilityMote> orphans = new HashSet<>(motes);

        for (MoteGroup group : groups) {
            orphans.removeAll(group.getMoteList());
        }

        initialGroup.clear();
        if (!orphans.isEmpty()) {
            // Add all to initial group
            initialGroup.addAll(orphans);
        }

        // Save expansion state
        Set<MoteGroup> expandedGroups = expandedGroups();
        Set<DefaultMutableTreeNode> expansionNodes = new HashSet<>();

        // Create top node
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Groups");
        DefaultMutableTreeNode initialNode = initialGroup.getTreeNode();
        top.add(initialNode);
        if (expandedGroups.contains(initialGroup)) {
            expansionNodes.add(initialNode);
        }

        // Sort on group name
        groups.sort(Comparator.comparing(g -> g.getGroupName()));

        // Add groups to top node
        for (MoteGroup group : groups) {
            DefaultMutableTreeNode groupNode = group.getTreeNode();
            top.add(groupNode);

            if (expandedGroups.contains(group)) {
                expansionNodes.add(groupNode);
            }
        }

        moteTree.setModel(new DefaultTreeModel(top));

        for (DefaultMutableTreeNode node : expansionNodes) {
            TreePath path = new TreePath(node.getPath());
            moteTree.expandPath(path);
        }

        listeners.forEach(MoteGroupUpdateListener::moteGroupsUpdated);
    }

    public static void showGroupPanel() {
        instance.show();
        try {
            instance.setSelected(true);
        } catch (PropertyVetoException ignored) {
        }
    }

    public void addListener(MoteGroupUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MoteGroupUpdateListener listener) {
        listeners.remove(listener);
    }

    public List<MoteGroup> getGroups() {
        List<MoteGroup> g = new ArrayList<>(groups);
        g.add(initialGroup);
        return g;
    }

    public void refreshMotes() {
        motes = Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList());
        updateUIGroups();
    }
}
