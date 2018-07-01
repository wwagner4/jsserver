package vsoc.server.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldContentPanel extends JPanel implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 1L;

    FieldPanel fieldCanvas = new FieldPanel();

    JPanel ctrlPanel = new JPanel();

    JToggleButton speedUpButton = new JToggleButton("speed up (no animation)");

    JLabel speedLabel = new JLabel("speed");

    JSlider speedSlider = new JSlider();

    CtrlSimulation sim;

    public FieldContentPanel() {
        try {
            init();
            this.speedUpButton.setSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSim(CtrlSimulation s) {
        this.fieldCanvas.setSim(s);
        sim = s;
        ctrlSpeed();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.ctrlPanel.setLayout(new FlowLayout());
        speedSlider.setModel(speedSliderModel());
        speedSlider.addChangeListener(this);
        speedUpButton.addActionListener(this);
        this.ctrlPanel.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        this.add(this.fieldCanvas, BorderLayout.CENTER);
        this.add(this.ctrlPanel, BorderLayout.SOUTH);

        this.ctrlPanel.add(this.speedLabel);
        this.ctrlPanel.add(this.speedSlider);
        this.ctrlPanel.add(this.speedUpButton);

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == this.speedUpButton) {
            ctrlSpeed();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.speedSlider) {
            if (!this.speedUpButton.isSelected()) {
                sim.setDelay(adjust(this.speedSlider.getValue()));
            }
        }
    }

    private BoundedRangeModel speedSliderModel() {
        return new DefaultBoundedRangeModel(0, 5, 0, 100);
    }

    private void ctrlSpeed() {
        if (this.speedUpButton.isSelected()) {
            sim.setDelay(0);
            sim.setInformListeners(false);
        } else {
            sim.setDelay(adjust(this.speedSlider.getValue()));
            sim.setInformListeners(true);
        }
    }

    private int adjust(int value) {
        return 5 * value * value / 1000;
    }
}