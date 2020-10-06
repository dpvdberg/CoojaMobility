package gui;

import org.contikios.cooja.*;
import org.contikios.cooja.plugins.Visualizer;
import utils.MobilityMote;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@ClassDescription("Simulation Information")
@PluginType(PluginType.SIM_PLUGIN)
public class MoteGroupPanel extends VisPlugin {
    private final List<MobilityMote> motes;
    private List<MoteGroup> groups = new ArrayList<>();
    Visualizer visualizer = null;
    private JPanel mainPanel;
    private JTree moteTree;
    private JButton btnCreateGroup;
    private JButton btnRandomize;

    public class MoteGroup {
        private final String groupName;
        private final List<MobilityMote> moteList;

        public MoteGroup(String groupName) {
            this.groupName = groupName;
            moteList = new ArrayList<>();
        }

        public String getGroupName() {
            return groupName;
        }

        public List<MobilityMote> getMoteList() {
            return moteList;
        }

        public void addMote(MobilityMote mote) {
            moteList.add(mote);
        }

        public void addAll(Collection<MobilityMote> motes) {
            moteList.addAll(motes);
        }

        public void removeMote(MobilityMote mote) {
            moteList.remove(mote);
        }

        @Override
        public String toString() {
            return groupName;
        }

        public MutableTreeNode getTreeNode() {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
            for (MobilityMote mote : moteList) {
                node.add(new DefaultMutableTreeNode(mote));
            }
            return node;
        }
    }

    public MoteGroupPanel(Simulation simulation, final Cooja cooja) {
        super("Mobility groups", cooja);

        try {
            visualizer = (Visualizer) simulation.getCooja().getPlugin(Visualizer.class.getName());
        } catch (Exception ignored) {
            System.out.println("Could not get visualizer for mote group dialog");
        }

        motes = Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList());

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
    }

    public void setGroups(List<MoteGroup> groups) {
        this.groups = groups;
        updateUIGroups();
    }

    public void updateUIGroups() {
        if (groups.isEmpty()) {
            MoteGroup group = new MoteGroup("Initial Group");
            group.addAll(motes);

            groups.add(group);
        }

        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Groups");

        for (MoteGroup group : groups) {
            top.add(group.getTreeNode());
        }

        moteTree.setModel(new DefaultTreeModel(top));
    }
}
