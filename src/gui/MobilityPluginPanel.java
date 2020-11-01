package gui;

import gui.group.MoteGroupPanel;
import models.MobilityModel;
import models.MobilityModelFactory;
import org.contikios.cooja.Mote;
import org.contikios.cooja.SimEventCentral;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.plugins.Visualizer;
import utils.MobilityMote;
import utils.MoteGroup;
import utils.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MobilityPluginPanel {
    private final Simulation simulation;
    private JPanel mainPanel;
    private JComboBox<MobilityModel> modelComboBox;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel mobilityModelSettings;
    private JSlider updateIntervalSlider;
    private JCheckBox chkStoreMovementHistory;
    private JButton btnSaveImage;
    private MobilityModel activeModel;
    private boolean isStarted = false;

    private TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    public long getPeriod() {
        // period in mobility models is in microseconds
        return updateIntervalSlider.getValue() * Simulation.MILLISECOND;
    }

    private static MobilityPluginPanel instance;

    public static MobilityPluginPanel getInstance() {
        return instance;
    }

    public static void buildInstance(Simulation simulation) {
        if (instance == null) {
            instance = new MobilityPluginPanel(simulation);
        }
    }


    private SimEventCentral.MoteCountListener nodeChangedObserver = new SimEventCentral.MoteCountListener() {
        public void moteWasAdded(Mote mote) {
            if (activeModel == null) {
                return;
            }
            activeModel.setMotes(Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList()));
            MoteGroupPanel.getInstance().refreshMotes();
        }
        public void moteWasRemoved(Mote mote) {
            if (activeModel == null) {
                return;
            }
            activeModel.setMotes(Arrays.stream(simulation.getMotes()).map(MobilityMote::new).collect(Collectors.toList()));
            MoteGroupPanel.getInstance().refreshMotes();
        }
    };

    private MobilityPluginPanel(Simulation simulation) {
        this.simulation = simulation;
        simulation.getEventCentral().addMoteCountListener(nodeChangedObserver);

        modelComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                MobilityModel selectedModel = (MobilityModel) modelComboBox.getSelectedItem();
                assert selectedModel != null;
                selectedModel.setPeriod(getPeriod());

                if (activeModel != null && isStarted) {
                    // actively switch model
                    activeModel.unregister();
                    selectedModel.register();
                }

                activeModel = selectedModel;

                String title = activeModel.getMobilityModelName() + " settings";
                titledBorder.setTitle(title);
                mobilityModelSettings.setBorder(titledBorder);

                // Set mobility model settings panel
                mobilityModelSettings.removeAll();
                mobilityModelSettings.add(activeModel.getModelSettingsComponent());

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        chkStoreMovementHistory.addItemListener(e -> {
            boolean selected = chkStoreMovementHistory.isSelected();
            Map<MobilityMote, List<Vector>> map = activeModel.setStorePositionHistory(selected);

            MoteMovementHistorySkin.setActive(selected);
            MoteMovementHistorySkin.setMobilityHistoryMap(map);
        });

        btnSaveImage.addActionListener(e -> {
            Visualizer visualizer = (Visualizer) simulation.getCooja().getPlugin(Visualizer.class.getName());

            if (visualizer != null) {
                BufferedImage img = new BufferedImage(visualizer.getWidth(), visualizer.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = img.getGraphics();
                visualizer.paint(g);
                g.dispose();

                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showSaveDialog(visualizer);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File outDir = fc.getSelectedFile();

                    File imageFile = new File(outDir,
                            new SimpleDateFormat("'visualizer-'yyyyMMddHHmm'.png'").format(new Date()));
                    try {
                        ImageIO.write(img, "png", imageFile);
                        System.out.println("Image file saved: " + imageFile.getAbsolutePath());
                    } catch (IOException ignored) {
                        System.out.println("Could not write image!");
                    }
                }
            }
        });

        updateIntervalSlider.addChangeListener(e -> activeModel.setPeriod(getPeriod()));

        modelComboBox.setModel(new DefaultComboBoxModel<>());
        for (MobilityModel model : MobilityModelFactory.buildModels(simulation)) {
            modelComboBox.addItem(model);
        }

        btnStart.addActionListener(e -> start());
        btnStop.addActionListener(e -> stop());

        Visualizer.registerVisualizerSkin(MoteMovementHistorySkin.class);
        System.out.println("Mobility plugin instance created");

        Visualizer visualizer = (Visualizer) simulation.getCooja().getPlugin(Visualizer.class.getName());
        if (visualizer != null) {
            System.out.println("Movement history visualizer skin compatible with simulation: " + visualizer.isSkinCompatible(MoteMovementHistorySkin.class));
        }

        btnStop.setEnabled(false);
    }

    private void start() {
        if (!isStarted) {
            isStarted = true;
            activeModel.register();

            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        }
    }

    private void stop() {
        if (isStarted) {
            isStarted = false;
            activeModel.unregister();

            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
