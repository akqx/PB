import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class HistogramFrame extends JFrame implements ActionListener {

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem miRGBAvg, miRGB, miRed, miGreen, miBlue;

	private int menuChoose = 1; // 0 - RGB uœredniony, 1 - RGB skumulowany, 2 -
								// Red, 3 - Green, 4 - Blue;

	private int[] red = new int[256];
	private int[] green = new int[256];
	private int[] blue = new int[256];
	private int[] RGBAverage = new int[256];

	private int maxValue = 0;
	private int scale = 1;

	public HistogramFrame() {
		super("Histogram");
		getContentPane().setBackground(Color.WHITE);

		setSize(350, 250);
		setResizable(false);
		setAlwaysOnTop(true);
		setLocation(1000, 450);

		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

		menu = new JMenu("Kana³: R + G + B");
		menuBar.add(menu);

		menu.addSeparator();

		miRGB = new JMenuItem("R + G + B");
		miRGB.setBackground(Color.WHITE);
		miRGB.addActionListener(this);
		menu.add(miRGB);

		menu.addSeparator();

		miRGBAvg = new JMenuItem("RGB");
		miRGBAvg.setBackground(Color.WHITE);
		miRGBAvg.addActionListener(this);
		menu.add(miRGBAvg);

		miRed = new JMenuItem("Red");
		miRed.setBackground(Color.WHITE);
		miRed.addActionListener(this);
		menu.add(miRed);

		miGreen = new JMenuItem("Green");
		miGreen.setBackground(Color.WHITE);
		miGreen.addActionListener(this);
		menu.add(miGreen);

		miBlue = new JMenuItem("Blue");
		miBlue.setBackground(Color.WHITE);
		menu.add(miBlue);

		setJMenuBar(menuBar);
	}

	public void calculateHistogram(BufferedImage image) {
		red = new int[256];
		green = new int[256];
		blue = new int[256];
		RGBAverage = new int[256];
		maxValue = 0;
		scale = 0;

		int[] pixel = new int[4];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				pixel = image.getRaster().getPixel(i, j, new int[4]);

				red[pixel[0]]++;
				green[pixel[1]]++;
				blue[pixel[2]]++;
			}
		}

		for (int i = 0; i < 256; i++) {
			if (red[i] > maxValue)
				maxValue = red[i];
			else if (green[i] > maxValue)
				maxValue = green[i];
			else if (blue[i] > maxValue)
				maxValue = blue[i];

			RGBAverage[i] = (red[i] + green[i] + blue[i]) / 3;
		}

		scale = maxValue / 150;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (menuChoose == 1) {
			for (int i = 0; i < 256; i++) {
				g.setColor(Color.RED);
				g.drawLine(50 + i, 225, 50 + i, 225 - red[i] / scale);
				g.setColor(Color.GREEN);
				g.drawLine(50 + i, 225, 50 + i, 225 - green[i] / scale);
				g.setColor(Color.BLUE);
				g.drawLine(50 + i, 225, 50 + i, 225 - blue[i] / scale);
			}
		} else {

			int[] tmp = new int[256];
			if (menuChoose == 0) {
				g.setColor(Color.DARK_GRAY);
				tmp = RGBAverage;
			} else if (menuChoose == 2) {
				g.setColor(Color.RED);
				tmp = red;
			} else if (menuChoose == 3) {
				g.setColor(Color.GREEN);
				tmp = green;
			} else if (menuChoose == 4) {
				g.setColor(Color.BLUE);
				tmp = blue;
			}

			for (int i = 0; i < 256; i++) {
				g.drawLine(50 + i, 225, 50 + i, 225 - tmp[i] / scale);
			}

		}

	}

	public int[] getRed() {
		return red;
	}

	public int[] getGreen() {
		return green;
	}

	public int[] getBlue() {
		return blue;
	}

	public int[] getRGB() {
		return RGBAverage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miRGBAvg) {
			menu.setText("Kana³: RGB");
			menuChoose = 0;
		} else if (e.getSource() == miRGB) {
			menu.setText("Kana³: R + G + B");
			menuChoose = 1;
		} else if (e.getSource() == miRed) {
			menu.setText("Kana³: Red");
			menuChoose = 2;
		} else if (e.getSource() == miGreen) {
			menu.setText("Kana³: Green");
			menuChoose = 3;
		} else if (e.getSource() == miBlue) {
			menu.setText("Kana³: Blue");
			menuChoose = 4;
		}

		repaint();
	}

}
