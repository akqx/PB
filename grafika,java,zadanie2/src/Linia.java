import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthSeparatorUI;

import java.awt.Font;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputEvent;

//je¿eli chce siê zobaczyæ kod do porszególnego zadania proszê wyszykaæ "ZADANIE NUMER (liczba)", tak bêdzie szybciej  

public class Linia extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	private JButton[] buttons = new JButton[7];// linia ->button[0],
												// prosotkat->button[1],okr¹g->button[2],dodana->button[3],
												// odejmowana->button[4],mnozona->[5],dzielona->button[6]
	private int licznik = 1;
	private JButton startDoing2D, skaluj, grayTwo, grayOne, filterOK, colorOK, histogramStreatch, binBut, binNBut;
	private BufferedImage actualImage, originalImage;
	private JLabel label1, label2, label3, label4, label5, colorRGB, colorPreview, actualImageScale, brightness,
			kontrast, colorFinder;
	private JLabel[] pozycjaSkalowanego = new JLabel[4];
	private JTextArea colorWynik;
	private JFileChooser chooser, saveChooser;
	private JScrollPane scrollImagePane;
	private JSlider zoomSlider, brightnessSlider, kontrastSlider;
	private JTextField oblongHeight, oblondWidth, circleR, lineX, lineY;
	private JTextField[] dzialania = new JTextField[4];// 1->dodawanie
														// 2->odejmowanie
														// 3->mnozenie
														// 4->dzielnie

	private JTextField[] pozycjaProsotkataSkalowanie = new JTextField[4];
	private JPanel contentPane, configurationPane, imagePane, dimentionsOblong, dimentionsCircle, dimentionsLine, panel,
			scalePanel;
	private JMenuBar menuBar;
	private JMenu mPlik, mObraz, mOkno;
	private JMenuItem miOpenImage, miSaveImage, miResetImage, miExit, miMedian3, miMedian5, miBinaryzacja,
			miShowHistogram, miHistogramEqualization;
	private JMenuItem[] filtrMorfo = new JMenuItem[5];
	private JTextField filterMask[][] = new JTextField[3][3];
	private JComboBox filterType;
	private JComboBox selectColor;
	private JTextField colorNumber;

	int id = 0, positionX, positionY, oHeightCheck = 0, oWidthCheck = 0, circleRCheck, lineXCheck = 0, lineYCheck = 0,
			dodanaCheck = 0, odejmowanaCheck = 0, mnozonaCheck = 1, dzielonaCheck = 1, iWidth, iHeight,
			skalowanyX1Check = 0, skalowanyY1Check = 0, skalowanyX2Check = 0, skalowanyY2Check = 0;
	float skladowaColorLicznik = 0, RGBcolorLicznik = 0, greenLicznik = 0, colorLicznik = 0, skladowaCheck = 0,
			sumaColorow;
	private Robot robot;
	private Point pInvert = null;
	private HistogramFrame histogram;
	private JTextField jtfVMin, jtfVMax, jtfTr, jtfRozmiarOkna, jtfProg;

	// do zadania 7
	private Point2D[] punkty;
	private int aktualnaPozycja;
	private Point2D[] punktyWlasne;

	// do zadania 2
	private JTextArea PPM;
	private JButton PPMSearch;
	private Scanner br = null;
	private String P3 = "P3";
	private String P6 = "P6";
	private String name;
	float k;
	int kolumny, wiersze, maxLiczbaSkladowej;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ******************************************************************************************************************************//
	// POCZ¥TEK TWORZENIA OKEINKA //
	// ******************************************************************************************************************************//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Linia() {

		super("Linia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1350, 700);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

		mPlik = new JMenu("Plik");
		menuBar.add(mPlik);

		miOpenImage = new JMenuItem("Otwórz obraz");
		miOpenImage.setBackground(Color.WHITE);
		miOpenImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		miOpenImage.addActionListener(this);
		mPlik.add(miOpenImage);

		miSaveImage = new JMenuItem("Zapisz obraz");
		miSaveImage.setBackground(Color.WHITE);
		miSaveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSaveImage.addActionListener(this);
		mPlik.add(miSaveImage);
		mPlik.addSeparator();

		miResetImage = new JMenuItem("Zresetuj obraz");
		miResetImage.setBackground(Color.WHITE);
		miResetImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		miResetImage.addActionListener(this);
		mPlik.add(miResetImage);
		mPlik.addSeparator();

		miExit = new JMenuItem("Zakoñcz");
		miExit.setBackground(Color.WHITE);
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		miExit.addActionListener(this);
		mPlik.add(miExit);

		mObraz = new JMenu("Obraz");
		mObraz.setBackground(Color.DARK_GRAY);
		menuBar.add(mObraz);

		mOkno = new JMenu("Okno");
		mOkno.setBackground(Color.DARK_GRAY);
		menuBar.add(mOkno);
		mObraz.addSeparator();

		JMenu mnFiltry = new JMenu("Filtry");
		mnFiltry.setBackground(Color.WHITE);
		mObraz.add(mnFiltry);

		mnFiltry.addSeparator();

		miMedian3 = new JMenuItem("Filtr medianowy 3x3");
		miMedian3.setBackground(Color.WHITE);
		miMedian3.addActionListener(this);
		mnFiltry.add(miMedian3);

		miMedian5 = new JMenuItem("Filtr medianowy 5x5");
		miMedian5.setBackground(Color.WHITE);
		miMedian5.addActionListener(this);
		mnFiltry.add(miMedian5);
		mnFiltry.addSeparator();
		filtrMorfo[0] = new JMenuItem("Dylacja");
		filtrMorfo[1] = new JMenuItem("Erozja");
		filtrMorfo[2] = new JMenuItem("Otwarcie");
		filtrMorfo[3] = new JMenuItem("Domkniecie");
		filtrMorfo[4] = new JMenuItem("Hit-or-miss");

		for (int i = 0; i < 5; i++) {
			filtrMorfo[i].setBackground(Color.WHITE);
			filtrMorfo[i].addActionListener(this);
			mnFiltry.add(filtrMorfo[i]);

		}

		setJMenuBar(menuBar);

		configurationPane = new JPanel();
		configurationPane.setBackground(Color.WHITE);
		configurationPane.setBounds(0, 0, 570, 700);
		configurationPane.setLayout(null);
		contentPane.add(configurationPane);

		zoomSlider = new JSlider(JSlider.HORIZONTAL, 1, 1350, 1);
		zoomSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		zoomSlider.setBackground(Color.WHITE);
		zoomSlider.setValue(100);
		zoomSlider.setBounds(25, 105, 250, 40);
		zoomSlider.setMinorTickSpacing(25);
		zoomSlider.setMajorTickSpacing(200);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addMouseMotionListener(this);
		zoomSlider.addMouseListener(this);
		configurationPane.add(zoomSlider);

		// POPRAWIÆ!!! dodaæ przycisk do skalowania
		punkty = new Point2D[2];
		punkty[0] = new Point2D.Double(skalowanyX1Check, skalowanyY1Check);
		punkty[1] = new Point2D.Double(skalowanyX2Check, skalowanyY2Check);

		// PANEL GDZIE BÊDZIE NASZ OBRAZ
		imagePane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				// malowanie naszego prostokata z zdania 7//
				Graphics2D g2React = (Graphics2D) g;
				for (Point2D point : punkty) {
					/// wytworzenie tych czarncyh kwadratów do chywtania podczas
					/// skalowania -10 bo chemy zeby nasz punkt by³ po œordku
					/// lini, ¿eby to ³adnie wygl¹da³o, to bedzie tylko do
					/// rêcznego skalowania
					double x = point.getX() - 10;
					double y = point.getY() - 10;
					g2React.fill(new Rectangle2D.Double(x, y, 20, 20));
				}

				Rectangle2D r = new Rectangle2D.Double();
				r.setFrameFromDiagonal(punkty[0], punkty[1]);
				g2React.draw(r);

				try {
					// mie¿emy punkt i malujemy na bia³o
					if (pInvert != null) {
						Color col = new Color(255, 255, 255);

						// id=1 ¿e jesteœmy na panelu malowania linii
						if (id == 1) {
							try {
								// pobieramy z panelu nasze wspó³rzêdne
								// pocz¹tkowe
								getLineStartPosition();
								// metoda tworz¹ca liniê
								line(col, lineXCheck, lineYCheck, pInvert.x, pInvert.y);
							}
							// je¿eli nic nie wpiszemy pojawi siê komunikat
							catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(imagePane, "Proszê wpisaæ liczbê!");
							}
						}

						// id=2 ¿e jesteœmy na panelu malowania kwadratu
						if (id == 2) {
							// pobieranie danych z panelu
							try {
								getValues();
								drawRect(col, pInvert.x, pInvert.y, oWidthCheck, oHeightCheck);
							}
							// je¿eli nic nie wpiszemy pojawi siê komunikat
							catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(imagePane, "Proszê wpisaæ liczbê!");
							}

						}

						// id=3 ¿e jesteœmy na panelu malowania okrêgu
						if (id == 3) {
							// pobieranie wartoœci z panelu, to jest naszej
							// œrednicy
							try {
								getR();
								CirclePolar(col, pInvert.x, pInvert.y, circleRCheck);
							}
							// je¿eli nic nie wpiszemy pojawi siê komunikat
							catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(imagePane, "Proszê wpisaæ liczbê!");
							}
						}
						if (id == 4) {
							// ZACZNIJ DZIA£AÆ NA METODY 2D
						}

						Graphics2D g2 = actualImage.createGraphics();
						g2.dispose();
					}
				} catch (NullPointerException e) {
				}
				g2d.drawImage(actualImage, 0, 0, null);
			}
		};

		imagePane.setLayout(null);
		imagePane.addMouseMotionListener(this);
		imagePane.addMouseListener(this);

		scrollImagePane = new JScrollPane(imagePane);
		scrollImagePane.setPreferredSize(new Dimension(1045, 650));
		scrollImagePane.setBorder(null);
		scrollImagePane.setBounds(570, 0, 1045, 650);
		scrollImagePane.setAutoscrolls(true);
		contentPane.add(scrollImagePane);

		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "ppm", "gif",
				"png");
		chooser.setFileFilter(filter);
		saveChooser = new JFileChooser();

		colorRGB = new JLabel("Kolor RGB: ---");
		colorRGB.setFont(new Font("Arial", Font.PLAIN, 12));
		colorRGB.setBounds(75, 25, 250, 40);
		configurationPane.add(colorRGB);

		colorPreview = new JLabel();
		colorPreview.setBounds(25, 25, 40, 40);
		colorPreview.setOpaque(true);
		colorPreview.setBackground(Color.WHITE);
		configurationPane.add(colorPreview);

		actualImageScale = new JLabel("Rozmiar obrazu: 100%");
		actualImageScale.setFont(new Font("Arial", Font.PLAIN, 12));
		actualImageScale.setBounds(25, 85, 250, 12);
		configurationPane.add(actualImageScale);

		brightness = new JLabel("Jasnoœæ: 100%");
		brightness.setFont(new Font("Arial", Font.PLAIN, 12));
		brightness.setBounds(25, 145, 250, 40);
		configurationPane.add(brightness);

		kontrast = new JLabel("Kontrast: 100%");
		kontrast.setFont(new Font("Arial", Font.PLAIN, 12));
		kontrast.setBounds(25, 220, 250, 40);
		configurationPane.add(kontrast);

		brightnessSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		brightnessSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		brightnessSlider.setBackground(Color.WHITE);
		brightnessSlider.setValue(100);
		brightnessSlider.setBounds(25, 180, 250, 40);
		brightnessSlider.setMinorTickSpacing(25);
		brightnessSlider.setMajorTickSpacing(100);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.addMouseMotionListener(this);
		brightnessSlider.addMouseListener(this);
		configurationPane.add(brightnessSlider);

		kontrastSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
		kontrastSlider.setFont(new Font("Tahoma", Font.PLAIN, 11));
		kontrastSlider.setBackground(Color.WHITE);
		kontrastSlider.setValue(100);
		kontrastSlider.setBounds(25, 255, 250, 40);
		kontrastSlider.setMinorTickSpacing(25);
		kontrastSlider.setMajorTickSpacing(100);
		kontrastSlider.setPaintTicks(true);
		kontrastSlider.setPaintLabels(true);
		kontrastSlider.addMouseMotionListener(this);
		kontrastSlider.addMouseListener(this);
		configurationPane.add(kontrastSlider);

		grayOne = new JButton("szaroœci- sposób 1");
		grayOne.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayOne.setBorderPainted(false);
		grayOne.setBackground(Color.ORANGE);
		grayOne.setBounds(15, 320, 130, 30);
		grayOne.addActionListener(this);
		configurationPane.add(grayOne);
		grayOne.addMouseListener(this);

		grayTwo = new JButton("szaroœci- sposób 2");
		grayTwo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		grayTwo.setBorderPainted(false);
		grayTwo.setBackground(Color.ORANGE);
		grayTwo.setBounds(155, 320, 130, 30);
		grayTwo.addActionListener(this);
		configurationPane.add(grayTwo);
		grayTwo.addMouseListener(this);

		buttons[0] = new JButton("LINIA");
		buttons[0].setBackground(Color.ORANGE);
		buttons[0].setBounds(15, 450, 70, 30);
		buttons[0].addActionListener(this);
		configurationPane.add(buttons[0]);

		buttons[1] = new JButton("PROSTOK¥T");
		buttons[1].setBackground(Color.ORANGE);
		buttons[1].setBounds(90, 450, 110, 30);
		buttons[1].addActionListener(this);
		configurationPane.add(buttons[1]);

		buttons[2] = new JButton("OK¥G");
		buttons[2].setBounds(205, 450, 70, 30);
		buttons[2].addActionListener(this);
		configurationPane.add(buttons[2]);

		buttons[3] = new JButton("dodaj");
		buttons[4] = new JButton("odejmnij");
		buttons[5] = new JButton("pomno¿");
		buttons[6] = new JButton("przedziel");

		buttons[3] = new JButton("dodaj");
		buttons[3].setBounds(15, 360, 70, 30);

		buttons[4] = new JButton("odejmij");
		buttons[4].setBounds(155, 360, 70, 30);

		buttons[5] = new JButton("pomno¿");
		buttons[5].setBounds(15, 400, 70, 30);

		buttons[6] = new JButton("dziel");
		buttons[6].setBounds(155, 400, 70, 30);

		for (int i = 0; i < 7; i++) {
			buttons[i].setFont(new Font("Tahoma", Font.PLAIN, 10));
			buttons[i].setBorderPainted(false);
			buttons[i].setBackground(Color.ORANGE);
			buttons[i].addMouseListener(this);
			configurationPane.add(buttons[i]);

		}

		for (int i = 0; i < 4; i++) {
			dzialania[i] = new JTextField("1");
			dzialania[i].setBackground(Color.WHITE);
			dzialania[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
			dzialania[i].addMouseListener(this);
			dzialania[i].addKeyListener(this);
			configurationPane.add(dzialania[i]);
		}

		dzialania[0].setBounds(90, 360, 40, 30);
		dzialania[1].setBounds(230, 360, 40, 30);
		dzialania[2].setBounds(90, 400, 40, 30);
		dzialania[3].setBounds(230, 400, 40, 30);

		dimentionsOblong = new JPanel();
		dimentionsOblong.setBounds(0, 480, 300, 600);
		dimentionsOblong.setLayout(null);
		dimentionsOblong.setVisible(true);
		dimentionsOblong.setBackground(Color.ORANGE);
		configurationPane.add(dimentionsOblong);

		dimentionsCircle = new JPanel();
		dimentionsCircle.setBounds(0, 480, 300, 600);
		dimentionsCircle.setLayout(null);
		configurationPane.add(dimentionsCircle);
		dimentionsCircle.setVisible(true);
		dimentionsCircle.setBackground(Color.ORANGE);

		dimentionsLine = new JPanel();
		dimentionsLine.setBounds(0, 480, 300, 600);
		dimentionsLine.setLayout(null);
		dimentionsLine.setVisible(false);
		dimentionsLine.setBackground(Color.ORANGE);
		configurationPane.add(dimentionsLine);

		oblondWidth = new JTextField("0");
		oblondWidth.setBounds(120, 50, 100, 30);
		oblondWidth.setBackground(Color.WHITE);
		oblondWidth.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsOblong.add(oblondWidth);

		label1 = new JLabel("Wpisz szerokoœæ:");
		label1.setBounds(20, 50, 150, 40);
		label1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsOblong.add(label1);

		oblongHeight = new JTextField("0");
		oblongHeight.setBounds(120, 85, 100, 30);
		oblongHeight.setBackground(Color.WHITE);
		oblongHeight.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsOblong.add(oblongHeight);
		dimentionsOblong.setVisible(false);

		label2 = new JLabel("Wpisz wyskokoœæ:");
		label2.setBounds(20, 85, 150, 40);
		label2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsOblong.add(label2);

		circleR = new JTextField("0");
		circleR.setBounds(120, 50, 100, 30);
		circleR.setBackground(Color.WHITE);
		circleR.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsCircle.add(circleR);

		label3 = new JLabel("Podaj œrednicê:");
		label3.setBounds(20, 50, 150, 40);
		label3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsCircle.add(label3);
		dimentionsCircle.setVisible(false);

		oblongHeight.addKeyListener(this);
		oblondWidth.addKeyListener(this);
		circleR.addKeyListener(this);

		lineX = new JTextField("0");
		lineX.setBounds(120, 50, 100, 30);
		lineX.setBackground(Color.WHITE);
		lineX.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsLine.add(lineX);

		label4 = new JLabel("Punkt pocz¹tkowy X:");
		label4.setBounds(20, 50, 150, 40);
		label4.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsLine.add(label4);

		lineY = new JTextField("0");
		lineY.setBounds(120, 85, 100, 30);
		lineY.setBackground(Color.WHITE);
		lineY.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsLine.add(lineY);
		dimentionsLine.setVisible(false);

		label5 = new JLabel("Punkt pocz¹tko:wy Y:");
		label5.setBounds(20, 85, 150, 40);
		label5.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsLine.add(label5);

		JLabel lblFiltrowanieMaska = new JLabel("Filtry: ");
		lblFiltrowanieMaska.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFiltrowanieMaska.setBounds(35, 495, 250, 40);
		configurationPane.add(lblFiltrowanieMaska);

		filterOK = new JButton("OK");
		filterOK.setFont(new Font("Tahoma", Font.PLAIN, 12));
		filterOK.setBorderPainted(false);
		filterOK.setBackground(Color.LIGHT_GRAY);
		filterOK.setBounds(25, 600, 207, 25);
		filterOK.addActionListener(this);
		configurationPane.add(filterOK);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(55, 530, 150, 65);
		configurationPane.add(panel);
		panel.setLayout(new GridLayout(3, 3, 10, 1));

		filterType = new JComboBox();
		filterType.setModel(new DefaultComboBoxModel(new String[] { "Wyg³adzaj¹cy (uœrednij¹cy)", "Sobel",
				"Górnoprzepustowy", "Rozmycie Gaussowskie", "Splot maski" }));
		filterType.setBackground(Color.WHITE);
		filterType.setBounds(75, 500, 165, 25);
		filterType.addActionListener(this);
		configurationPane.add(filterType);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				filterMask[i][j] = new JTextField("1");
				panel.add(filterMask[i][j]);
			}
		}

		colorFinder = new JLabel("Szukaj koloru o padanej ska³dowej: ");
		colorFinder.setFont(new Font("Tahoma", Font.PLAIN, 12));
		colorFinder.setBounds(300, 35, 250, 40);
		configurationPane.add(colorFinder);

		selectColor = new JComboBox();
		selectColor.setModel(new DefaultComboBoxModel(new String[] { "Red", "Green", "Blue" }));
		selectColor.setBackground(Color.WHITE);
		selectColor.setBounds(300, 75, 150, 25);
		selectColor.addActionListener(this);
		configurationPane.add(selectColor);

		colorNumber = new JTextField("1");
		colorNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		colorNumber.setBounds(460, 75, 25, 25);
		colorNumber.addMouseListener(this);
		colorNumber.addKeyListener(this);
		configurationPane.add(colorNumber);

		colorWynik = new JTextArea("wynik:");
		colorWynik.setLineWrap(true);
		colorWynik.setWrapStyleWord(false);
		colorWynik.setFont(new Font("Tahoma", Font.PLAIN, 11));
		colorWynik.setBounds(300, 140, 150, 55);
		configurationPane.add(colorWynik);

		colorOK = new JButton("OK");
		colorOK.setFont(new Font("Tahoma", Font.PLAIN, 12));
		colorOK.setBorderPainted(false);
		colorOK.setBackground(Color.LIGHT_GRAY);
		colorOK.setBounds(300, 110, 180, 25);
		colorOK.addActionListener(this);
		configurationPane.add(colorOK);

		miShowHistogram = new JMenuItem("Histogram");
		miShowHistogram.setBackground(Color.WHITE);
		miShowHistogram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		miShowHistogram.addActionListener(this);
		mOkno.add(miShowHistogram);

		miHistogramEqualization = new JMenuItem("Wyrównaj histogram");
		miHistogramEqualization.setBackground(Color.WHITE);
		miHistogramEqualization
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		miHistogramEqualization.addActionListener(this);
		mObraz.add(miHistogramEqualization);

		mObraz.addSeparator();

		miBinaryzacja = new JMenuItem("Binaryzacja");
		miBinaryzacja.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		miBinaryzacja.setBackground(Color.WHITE);
		miBinaryzacja.addActionListener(this);
		mObraz.add(miBinaryzacja);

		mObraz.addSeparator();

		JLabel j1 = new JLabel("Rozci¹ganie histogramu");
		j1.setFont(new Font("Arial", Font.PLAIN, 12));
		j1.setBounds(325, 215, 250, 40);
		configurationPane.add(j1);

		jtfVMin = new JTextField("1");
		jtfVMin.setBounds(325, 255, 70, 25);
		configurationPane.add(jtfVMin);

		jtfVMax = new JTextField("255");
		jtfVMax.setBounds(405, 255, 70, 25);
		configurationPane.add(jtfVMax);

		histogramStreatch = new JButton("OK");
		histogramStreatch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		histogramStreatch.setBackground(Color.ORANGE);
		histogramStreatch.setBorderPainted(false);
		histogramStreatch.setBounds(483, 255, 50, 25);
		histogramStreatch.addActionListener(this);
		configurationPane.add(histogramStreatch);

		JLabel j2 = new JLabel("Binaryzacja");
		j2.setFont(new Font("Arial", Font.PLAIN, 12));
		j2.setBounds(325, 282, 250, 40);
		configurationPane.add(j2);

		jtfTr = new JTextField("128");
		jtfTr.setBounds(405, 290, 70, 25);
		configurationPane.add(jtfTr);

		binBut = new JButton("OK");
		binBut.setFont(new Font("Tahoma", Font.PLAIN, 12));
		binBut.setBackground(Color.ORANGE);
		binBut.setBorderPainted(false);
		binBut.setBounds(483, 290, 50, 25);
		binBut.addActionListener(this);
		configurationPane.add(binBut);

		JLabel j3 = new JLabel("Binaryzacja lokalna");
		j3.setFont(new Font("Arial", Font.PLAIN, 12));
		j3.setBounds(325, 315, 250, 40);
		configurationPane.add(j3);

		JLabel j4 = new JLabel("Rozmiar okna");
		j4.setFont(new Font("Arial", Font.PLAIN, 10));
		j4.setBounds(405, 330, 250, 40);
		configurationPane.add(j4);

		JLabel j5 = new JLabel("Parametr k");
		j5.setFont(new Font("Arial", Font.PLAIN, 10));
		j5.setBounds(325, 330, 250, 40);
		configurationPane.add(j5);

		jtfRozmiarOkna = new JTextField("3");
		jtfRozmiarOkna.setBounds(325, 362, 70, 25);
		configurationPane.add(jtfRozmiarOkna);

		jtfProg = new JTextField("-0.2");
		jtfProg.setBounds(405, 362, 70, 25);
		configurationPane.add(jtfProg);

		binNBut = new JButton("OK");
		binNBut.setFont(new Font("Tahoma", Font.PLAIN, 12));
		binNBut.setBackground(Color.ORANGE);
		binNBut.setBorderPainted(false);
		binNBut.setBounds(483, 362, 50, 25);
		binNBut.addActionListener(this);
		configurationPane.add(binNBut);

		startDoing2D = new JButton("Zacznij przekszta³caæ 2D");
		startDoing2D.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startDoing2D.setBackground(Color.ORANGE);
		startDoing2D.setBounds(303, 402, 250, 25);
		startDoing2D.addActionListener(this);
		startDoing2D.addMouseListener(this);
		configurationPane.add(startDoing2D);

		scalePanel = new JPanel();
		scalePanel.setBounds(303, 430, 250, 200);
		scalePanel.setLayout(null);
		scalePanel.setVisible(false);
		scalePanel.setBackground(Color.LIGHT_GRAY);
		configurationPane.add(scalePanel);

		pozycjaProsotkataSkalowanie[0] = new JTextField("0");
		pozycjaProsotkataSkalowanie[1] = new JTextField("0");
		pozycjaProsotkataSkalowanie[2] = new JTextField("0");
		pozycjaProsotkataSkalowanie[3] = new JTextField("0");
		for (int i = 0; i < 4; i++) {
			pozycjaProsotkataSkalowanie[i].setBackground(Color.WHITE);
			pozycjaProsotkataSkalowanie[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
			pozycjaProsotkataSkalowanie[i].addMouseListener(this);
			pozycjaProsotkataSkalowanie[i].addKeyListener(this);
			scalePanel.add(pozycjaProsotkataSkalowanie[i]);
		}

		pozycjaProsotkataSkalowanie[0].setBounds(30, 10, 30, 30);
		pozycjaProsotkataSkalowanie[1].setBounds(90, 10, 30, 30);
		pozycjaProsotkataSkalowanie[2].setBounds(150, 10, 30, 30);
		pozycjaProsotkataSkalowanie[3].setBounds(210, 10, 30, 30);

		pozycjaSkalowanego[0] = new JLabel("x1:");
		pozycjaSkalowanego[1] = new JLabel("y1:");
		pozycjaSkalowanego[2] = new JLabel("x2:");
		pozycjaSkalowanego[3] = new JLabel("%");

		for (int i = 0; i < 4; i++) {
			pozycjaSkalowanego[i].setBackground(Color.WHITE);
			scalePanel.add(pozycjaSkalowanego[i]);
		}
		pozycjaSkalowanego[0].setBounds(10, 10, 30, 30);
		pozycjaSkalowanego[1].setBounds(70, 10, 30, 30);
		pozycjaSkalowanego[2].setBounds(130, 10, 30, 30);
		pozycjaSkalowanego[3].setBounds(190, 10, 30, 30);

		skaluj = new JButton("skaluj");
		skaluj.setBackground(Color.ORANGE);
		skaluj.setFont(new Font("Tahoma", Font.PLAIN, 11));
		skaluj.addMouseListener(this);
		skaluj.setBounds(10, 50, 230, 30);
		scalePanel.add(skaluj);
		histogram = new HistogramFrame();

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ******************************************************************************************************************************//
	// KONIEC TOWORZENIA WYGLADU: POCZ¥TEK METOD //
	// ******************************************************************************************************************************//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getThisX(MouseEvent e) {
		return e.getX();
	}

	public int getThisY(MouseEvent e) {
		return e.getY();
	}

	// pobieranie pozycji do metody z prostok¹tem
	private void getValues() {
		oHeightCheck = Integer.parseInt(oblongHeight.getText());
		oWidthCheck = Integer.parseInt(oblondWidth.getText());

	}

	// pobieranie pozycji do metody z linia
	private void getLineStartPosition() {
		lineYCheck = Integer.parseInt(lineY.getText());
		lineXCheck = Integer.parseInt(lineX.getText());

	}

	// pobieranie z textFielda naszej œrednicy
	private void getR() {
		circleRCheck = Integer.parseInt(circleR.getText());
	}

	// pobieranie z textFiela neszej dodanej
	private void getDodana() {
		dodanaCheck = Integer.parseInt(dzialania[0].getText());
	}

	// pobieranie z textFielda naszej odejmowanej
	private void getOdejmowana() {
		odejmowanaCheck = Integer.parseInt(dzialania[1].getText());
	}

	// pobieranie z textFielda naszej mnoznonej
	private void getMnozona() {
		mnozonaCheck = Integer.parseInt(dzialania[2].getText());
	}

	// pobieranie z textFielda dzielonej
	private void getDzielona() {
		dzielonaCheck = Integer.parseInt(dzialania[3].getText());
	}

	private void getSkladowa() {
		skladowaCheck = Integer.parseInt(colorNumber.getText());
	}

	// pobieranie wartosci do utworzenia prostokata w podancych punktach
	private void getpozycjaSkalowanie() {
		skalowanyX1Check = Integer.parseInt(pozycjaProsotkataSkalowanie[0].getText());
		skalowanyY1Check = Integer.parseInt(pozycjaProsotkataSkalowanie[1].getText());
		skalowanyX2Check = Integer.parseInt(pozycjaProsotkataSkalowanie[2].getText());
		// !!!to jednak bedzie ile razy skalowany!!!
		// POPRAWIÆ!!!!
		skalowanyY2Check = Integer.parseInt(pozycjaProsotkataSkalowanie[3].getText());

	}
	// **************************************************************************************************************
	// ZADANIE NUMER 1
	// **************************************************************************************************************

	// wytworzenie linii
	public void line(Color c, int x, int y, int x2, int y2) {
		int color = c.getRGB();
		int w = x2 - x;
		int h = y2 - y;
		double m = h / (double) w;
		double j = y;
		for (int i = x; i <= x2; i++) {
			actualImage.setRGB(i, (int) j, color);
			j += m;
		}
	}

	// wytworzenie okêgu
	public void CirclePolar(Color c, int xc, int yc, int r) {
		int color = c.getRGB();
		double x, y;
		for (double i = 1.0 / r; i <= 2 * Math.PI; i += 1.0 / r) {
			y = yc + r * Math.sin(i);
			x = xc + r * Math.cos(i);
			actualImage.setRGB((int) x, (int) y, color);
		}
	}

	// wytworzenie prostok¹ta
	public void drawRect(Color c, int x1, int y1, int width, int height) {
		int color = c.getRGB();
		for (int x = x1; x < x1 + width; x++) {
			for (int y = y1; y < y1 + height; y++) {
				actualImage.setRGB(x, y, color);

			}
		}

		repaint();
	}

	private void resize(double ratio) {
		BufferedImage newImage = new BufferedImage((int) (actualImage.getWidth() * ratio),
				(int) (actualImage.getHeight() * ratio), BufferedImage.TYPE_INT_RGB);

		int[] pixel = new int[4];
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel((int) (i / ratio), (int) (j / ratio), new int[4]);
				newImage.getRaster().setPixels(i, j, 1, 1, new int[] { pixel[0], pixel[1], pixel[2], pixel[3] });
			}
		}
		actualImage = newImage;
	}

	// resetowanie, obrazu po zmianinie pozycji elementów, i po przyœcnieciu
	// CTR+r
	private void reset() {
		if (actualImage != null) {
			actualImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = actualImage.createGraphics();
			g.drawImage(originalImage, 0, 0, null);
		}
	}

	// **************************************************************************************************************
	// ZADANIE NUMER 2
	// **************************************************************************************************************

	private void convertingPPM() {
		// jezeli nasza koñcówka nazwy pliku konczy siê na ".png" to otwieramy
		// t¹ metodê (jest to w dalszej czeœci kodu)
		try {
			br = new Scanner(new BufferedReader(new FileReader(chooser.getSelectedFile())));
		}
		// komunickajca b³êdów
		catch (FileNotFoundException e) {
			System.out.println("Nie znaleziono pliku");
		}
		// wczytanie pierwszej lini nazwy P3 lub P6
		name = br.next();

		if (name.equals(P3)) {
			br.nextLine();
			System.out.println("KOMENTARZ PLIKU: " + br.nextLine());
			// wczytanie po 2 linii, pierwszego inta
			kolumny = br.nextInt();
			System.out.println("Kolumny:" + kolumny);
			// wczytanie wierszy
			wiersze = br.nextInt();
			System.out.println("Kolumny:" + wiersze);
			// maksymalna liczba sk³adowej przez któr¹ bêdzie skalowanie
			maxLiczbaSkladowej = br.nextInt();
			System.out.println("Max liczba ska³dowej:" + maxLiczbaSkladowej);
			// utworzenie naszego obrazu
			BufferedImage img = new BufferedImage(kolumny, wiersze, BufferedImage.TYPE_INT_RGB);
			File f = null;
			// nasza liczba skalowania liniowego obrazy
			k = 255 / maxLiczbaSkladowej;
			// wprowadzenie skladowych do obrazu
			for (int y = 0; y < wiersze; y++) {
				for (int x = 0; x < kolumny; x++) {
					Color color = new Color((int) (br.nextInt() * k), (int) (br.nextInt() * k),
							(int) (br.nextInt() * k));
					int RGB = color.getRGB();
					img.setRGB(x, y, RGB);
				}
			}
			// wprowadzenie zmian na panelu, nasz obraz aktualny jest taki jak
			// PPM P3
			actualImage = img;
			originalImage = img;
			iWidth = actualImage.getWidth();
			iHeight = actualImage.getHeight();
			imagePane.setPreferredSize(new Dimension(actualImage.getWidth() * zoomSlider.getValue() / 100,
					actualImage.getHeight() * zoomSlider.getValue() / 100));
			imagePane.revalidate();
			imagePane.repaint();
			// informacja o zakoñczeniu przetwarzania zdjecia, je¿eli siê
			// pojawi³a, a zdjêcia nie ma na panelu, coœ jest nie tak
			System.out.println("koniec przetwarzania zdjêcia");

		}
		// tak samo jak w P3, ale szukamy pierwszej linii o nazwie P6
		else if (name.equals(P6)) {

			br.nextLine();
			System.out.println("KOMENTARZ PLIKU: " + br.nextLine());

			kolumny = br.nextInt();
			System.out.println("Kolumny:" + kolumny);

			wiersze = br.nextInt();
			System.out.println("Kolumny:" + wiersze);

			maxLiczbaSkladowej = br.nextInt();
			System.out.println("Max liczba ska³dowej:" + maxLiczbaSkladowej);

			BufferedImage img = new BufferedImage(kolumny, wiersze, BufferedImage.TYPE_INT_RGB);
			File f = null;

			k = 255 / maxLiczbaSkladowej;
			System.out.println("zdjêcie jest w formacie P6-jest wczytywanie,dancyh, nie ma wyœwietlania");
			while (br.hasNext()) {
				String linia = br.next();
				for (int i = 0; i < linia.length(); i++) {
					// chcia³am takim sposobem wype³niæ ska³dowe
					// char redChar = linia.charAt(i);
					// int R = (int) redChar;

					// dane siê
					// wczytuja, ale mia³am progblem z liczb¹ CHARÓW i liczb¹
					// wierszy, zdjêcie w formacie p6 siê nie wyœwietla, ale
					// wczytuje, je¿eli usuniemy poni¿sz¹ liniê, zobaczymy nasze
					// ska³dowe
					// System.out.println((int) linia.charAt(i));

				}

			}
		}

	}

	// **************************************************************************************************************
	// ZADANIE NUMER 4
	// **************************************************************************************************************

	// zmianianie obrazu na aktualnie robione zmiany
	private void updateImage(int[] LUT) {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);
				actualImage.getRaster().setPixels(i, j, 1, 1,
						new int[] { LUT[pixel[0]], LUT[pixel[1]], LUT[pixel[2]], LUT[pixel[3]] });
			}
		}

		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		imagePane.repaint();
	}

	// zmianianie skali szaroœci sposób 1-> METODA ŒREDENIEJ
	private void greyscale() {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);
				int gs = (pixel[0] + pixel[1] + pixel[2]) / 3;

				actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { gs, gs, gs });
			}
		}
		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		imagePane.repaint();
	}

	// zmianianie skali szaroœci sposób 2-> METODA SPECJALNYCH WARTOŒCI
	// PODSTAWIONYCH I POMNO¯ONYCH
	private void greyscaleTwo() {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);

				double gs = (pixel[0] * 0.3 + pixel[1] * 0.59 + pixel[2] * 0.11);
				actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { (int) gs, (int) gs, (int) gs });
			}
		}
		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		imagePane.repaint();
	}

	// dodawanie
	private void dodana() {
		getDodana();
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i + dodanaCheck;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);
	}

	// odejmnowanie
	private void odejmowana() {
		getOdejmowana();
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i - odejmowanaCheck;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);
	}

	// mno¿enie
	private void mno¿ona() {
		getMnozona();
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i * mnozonaCheck;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);
	}

	// dzielenie
	private void dzielena() {
		getDzielona();

		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i / dzielonaCheck;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);

	}

	// rozjaœnianie
	public void brightness(double brightnessValue) {
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i + brightnessValue * 10;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);
	}

	// kontrast
	public void kontrast(double kontrastValue) {
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = (kontrastValue) * (i - (255 / 2) + 255 / 2);
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}
		updateImage(LUT);
	}

	// filtracja

	private void runFilter() {
		// pobieranie danych z texfieldów
		// oblicznie sumy maski
		int maskSum = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// pobranie wartosci z neszych texfieldów
				maskSum += Integer.parseInt(filterMask[i][j].getText());
			}
		}
		// pobieranie aktualnego zdjecia
		BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(actualImage, 0, 0, null);
		// obliczanie smuy punktów R,G,B
		for (int i = 1; i < actualImage.getWidth() - 1; i++) {
			for (int j = 1; j < actualImage.getHeight() - 1; j++) {
				int redSum = 0, greenSum = 0, blueSum = 0;
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						redSum += actualImage.getRaster().getPixel(k, l, new int[4])[0]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
						greenSum += actualImage.getRaster().getPixel(k, l, new int[4])[1]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
						blueSum += actualImage.getRaster().getPixel(k, l, new int[4])[2]
								* Integer.parseInt(filterMask[i + 1 - k][j + 1 - l].getText());
					}
				}
				// filtracja
				if (maskSum != 0) {
					redSum /= maskSum;
					greenSum /= maskSum;
					blueSum /= maskSum;
				}
				// ogtaniczanie liczby od min=0 do max=255 bo rbg jest od 0 do
				// 256, i zaznaczemy nasze min i max, zeby zdjecie róznomierni
				// siê zmini³o, tj wczeœniej
				if (redSum > 255)
					redSum = 255;
				else if (redSum < 0)
					redSum = 0;
				if (greenSum > 255)
					greenSum = 255;
				else if (greenSum < 0)
					greenSum = 0;
				if (blueSum > 255)
					blueSum = 255;
				else if (blueSum < 0)
					blueSum = 0;
				// aktualzacja zdjecia poprzez nasza filtracje
				newImage.getRaster().setPixels(i, j, 1, 1, new int[] { redSum, greenSum, blueSum });
			}
		}

		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		actualImage = newImage;
		imagePane.repaint();
	}

	// filtr medianowy, róznica 3x3 lub 5x5 , jest w
	// menubar->obrazy->filtr->medninowy...
	private void medianFilter(int size) {
		// tak jak wczeœniej pobranie aktualnego zdjecia
		BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(actualImage, 0, 0, null);

		int margin = ((size - 1) / 2);
		for (int i = margin; i < actualImage.getWidth() - margin; i++) {
			for (int j = margin; j < actualImage.getHeight() - margin; j++) {
				// osobne tablice z wartoscami sk¹³dowych R,G,B
				int pxRed[] = new int[size * size];
				int pxGreen[] = new int[size * size];
				int pxBlue[] = new int[size * size];
				int a = 0;
				for (int k = i - margin; k <= i + margin; k++) {
					// wprowadzenie nowych wartosci pixeli
					for (int l = j - margin; l <= j + margin; l++) {
						pxRed[a] = actualImage.getRaster().getPixel(k, l, new int[4])[0];
						pxGreen[a] = actualImage.getRaster().getPixel(k, l, new int[4])[1];
						pxBlue[a] = actualImage.getRaster().getPixel(k, l, new int[4])[2];
						a++;
					}
				}
				// sortowanie tablic, bo jest to filtr z uporzatkowanymi
				// skia³dowymi, i jak nazwa zaka³ada, póiej wybranie mediany z
				// tej tablicy
				Arrays.sort(pxRed);
				Arrays.sort(pxGreen);
				Arrays.sort(pxBlue);
				// wytworzenie nowego obrazu z nowymi wartosciami, nasza mediana
				newImage.getRaster().setPixels(i, j, 1, 1,
						new int[] { pxRed[(a + 1) / 2], pxGreen[(a + 1) / 2], pxBlue[(a + 1) / 2] });
			}
		}
		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		actualImage = newImage;
		// zrealizowanie nowego obrazy na panelu
		imagePane.repaint();
	}

	// **************************************************************************************************************
	// ZADANIE NUMER 5
	// **************************************************************************************************************
	// dodaæ KOMENTARZE
	public void histogramStreatching(int vMin, int vMax) {
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			if (255 / (vMax - vMin) * (i - vMin) < 0)
				LUT[i] = 0;
			else if (255 / (vMax - vMin) * (i - vMin) > 255)
				LUT[i] = 255;
			else
				LUT[i] = 255 / (vMax - vMin) * (i - vMin);
		}

		updateImage(LUT);
	}

	public void histogramEqualization() {
		histogram.calculateHistogram(actualImage);

		int[] RGB = histogram.getRGB();
		double[] dRGB = new double[256];

		double sum = 0;
		int numberOfPixels = actualImage.getWidth() * actualImage.getHeight();
		for (int i = 0; i < 256; i++) {
			sum += RGB[i];
			dRGB[i] = (double) sum / numberOfPixels;
		}

		int[] LUT = new int[256];

		double dMin = 0;
		for (int i = 0; i < 256; i++) {
			if (dRGB[i] > 0) {
				dMin = dRGB[i];
				break;
			}
		}

		for (int i = 0; i < 256; i++) {
			LUT[i] = (int) ((double) ((dRGB[i] - dMin) / (1 - dMin)) * 255);
		}

		updateImage(LUT);
	}

	private void threshold(int T) {
		greyscale();

		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);
				if (pixel[0] < T)
					actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { 0, 0, 0 });
				else
					actualImage.getRaster().setPixels(i, j, 1, 1, new int[] { 255, 255, 255 });
			}
		}

		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		imagePane.repaint();
	}

	private void otsuThreshold() {
		greyscale();

		int[] hist = histogram.getRed();
		int total = actualImage.getHeight() * actualImage.getWidth();

		int sum = 0;
		for (int i = 0; i < 256; i++)
			sum += i * hist[i];

		int wB = 0, wF = 0, threshold = 0;
		double sumB = 0, varMax = 0;
		for (int i = 0; i < 256; i++) {
			wB += hist[i];
			if (wB == 0)
				continue;

			wF = total - wB;
			if (wF == 0)
				break;

			sumB += i * hist[i];

			double mB = sumB / wB;
			double mF = (sum - sumB) / wF;

			double varBetween = (double) wB * (double) wF * (mB - mF) * (mB - mF);
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}

		threshold(threshold);
	}

	private void niblackThreshold(int w, double k) {
		greyscale();

		BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(actualImage, 0, 0, null);

		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {

				// Okno
				int sum = 0, sum2 = 0, total = 0;
				for (int a = i - w / 2; a <= i + w / 2; a++) {
					if (a >= 0 && a < actualImage.getWidth()) {
						for (int b = j - w / 2; b <= j + w / 2; b++) {
							if (b >= 0 && b < actualImage.getHeight()) {
								sum += actualImage.getRaster().getPixel(a, b, new int[4])[0];
								sum2 += Math.pow(actualImage.getRaster().getPixel(a, b, new int[4])[0], 2);
								total++;
							}
						}
					}
				}

				int average = sum / total;

				double sDeviation = (double) Math.sqrt((double) (sum2 / total) - (average * average));
				int threshold = (int) (average + k * sDeviation);
				if (actualImage.getRaster().getPixel(i, j, new int[4])[0] < threshold)
					newImage.getRaster().setPixels(i, j, 1, 1, new int[] { 0, 0, 0 });
				else
					newImage.getRaster().setPixels(i, j, 1, 1, new int[] { 255, 255, 255 });
			}
		}

		actualImage = newImage;

		histogram.calculateHistogram(actualImage);
		histogram.repaint();
		imagePane.repaint();
	}

	// **************************************************************************************************************
	// ZADANIE NUMER 9
	// **************************************************************************************************************

	// ileczenie ile % obrazu stanowi kolor R lub G lub B oraz ile jest
	// procentowo sk³adowej podanje w textdieldzie
	private void howManyColor(int ktoryKolor) {
		getSkladowa();
		colorLicznik = 1;
		skladowaColorLicznik = 1;
		RGBcolorLicznik = 1;
		sumaColorow = 1;

		// pobranie aktualnego zdjecia
		BufferedImage newImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();

		int pixelColor[];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixelColor = actualImage.getRaster().getPixel(i, j, new int[3]);
				// liczenie ile jest pixeli
				colorLicznik++;
				sumaColorow = sumaColorow + pixelColor[0] + pixelColor[1] + pixelColor[2];

				RGBcolorLicznik = RGBcolorLicznik + pixelColor[ktoryKolor];
				// pobieranie wartosci naszego koloru
				if (pixelColor[ktoryKolor] == skladowaCheck) {
					skladowaColorLicznik++;
				}

			}
		}
	}

	// ******************************************************************************************************
	// *****************************************************************************************************

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Linia frame = new Linia();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ******************************************************************************************************
	// *****************************************************************************************************

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miOpenImage) {
			int returnVal = chooser.showOpenDialog(configurationPane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					// zadanie2
					// szukamy nazwy pliku o koñcówcê .ppm
					String ppm = ".ppm";
					String wybranyPlik = chooser.getSelectedFile().getName();

					if (wybranyPlik.indexOf(ppm) != -1) {
						System.out.println(chooser.getSelectedFile().getName());
						convertingPPM();
					} else {

						actualImage = ImageIO.read(chooser.getSelectedFile());
						originalImage = ImageIO.read(chooser.getSelectedFile());
						iWidth = actualImage.getWidth();
						iHeight = actualImage.getHeight();
						imagePane.setPreferredSize(new Dimension(actualImage.getWidth() * zoomSlider.getValue() / 100,
								actualImage.getHeight() * zoomSlider.getValue() / 100));
						imagePane.revalidate();
						imagePane.repaint();
					}
				} catch (IOException f) {
					f.printStackTrace();
				}

			}
		} else if (e.getSource() == miSaveImage) {
			Object[] stopien = { "1", "0.8", "0.6", "0.4" };

			String s = (String) JOptionPane.showInputDialog(contentPane, "Wybierz stopieñ kompresji obrazu: ",
					"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null, stopien, "1");

			
			System.out.println(s);
			if(s!=null){
			int returnVal = saveChooser.showSaveDialog(configurationPane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				float wartoscKompresji = Float.parseFloat(s);
				{
				
					try {
						// DO KOMPRESJI JPEGA
						JPEGImageWriteParam parametr = new JPEGImageWriteParam(null);
						parametr.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						parametr.setCompressionQuality(wartoscKompresji);
						final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
						writer.setOutput(new FileImageOutputStream(new File(saveChooser.getSelectedFile() + ".jpeg")));
						writer.write(null, new IIOImage(actualImage, null, null), parametr);

						// ImageIO.write(actualImage, "JPEG", new
						// File(saveChooser.getSelectedFile() + ".jpeg"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}}
			
			
			
		} else if (e.getSource() == miResetImage || licznik == 3) {
			reset();
			histogram.calculateHistogram(actualImage);
			histogram.repaint();
			zoomSlider.setValue(100);
			brightnessSlider.setValue(100);
			kontrastSlider.setValue(100);
			imagePane.repaint();
		}

		// Kolorowanie panelu, z elementatami do malowania
		for (int i = 0; i < 3; i++) {
			buttons[i].setBackground(Color.WHITE);
			dimentionsOblong.setVisible(false);
			dimentionsCircle.setVisible(false);
			dimentionsLine.setVisible(false);
			if (e.getSource() == buttons[i]) {
				buttons[i].setBackground(Color.ORANGE);
			}
		}

		if (e.getSource() == buttons[0]) {
			dimentionsLine.setVisible(true);
			id = 1;
		} else if (e.getSource() == buttons[1]) {
			dimentionsOblong.setVisible(true);
			id = 2;
		} else if (e.getSource() == buttons[2]) {
			dimentionsCircle.setVisible(true);
			id = 3;
		} else if (e.getSource() == startDoing2D) {
			scalePanel.setVisible(true);

			id = 4;
		}

		// filtry medianowe znajduje siê w Obrazy->filtry->medianowe
		else if (e.getSource() == miMedian3) {
			medianFilter(3);
		} else if (e.getSource() == miMedian5) {
			medianFilter(5);
		}
		// zrealizowanie naszego filtru z maski po kilnieciu buttona
		else if (e.getSource() == filterOK) {
			runFilter();
		}
		// wybranie i wprowadzeni do text fieldów wartosci wybranego filtra
		else if (e.getSource() == filterType) {
			if (filterType.getSelectedItem().equals("Wyg³adzaj¹cy (uœrednij¹cy)")) {
				filterMask[0][0].setText("1");
				filterMask[0][1].setText("1");
				filterMask[0][2].setText("1");
				filterMask[1][0].setText("1");
				filterMask[1][1].setText("1");
				filterMask[1][2].setText("1");
				filterMask[2][0].setText("1");
				filterMask[2][1].setText("1");
				filterMask[2][2].setText("1");
			}

			else if (filterType.getSelectedItem().equals("Sobel")) {
				filterMask[0][0].setText("1");
				filterMask[0][1].setText("2");
				filterMask[0][2].setText("1");
				filterMask[1][0].setText("0");
				filterMask[1][1].setText("0");
				filterMask[1][2].setText("0");
				filterMask[2][0].setText("-1");
				filterMask[2][1].setText("-2");
				filterMask[2][2].setText("-1");
			} else if (filterType.getSelectedItem().equals("Górnoprzepustowy")) {
				filterMask[0][0].setText("-1");
				filterMask[0][1].setText("-1");
				filterMask[0][2].setText("-1");
				filterMask[1][0].setText("-1");
				filterMask[1][1].setText("9");
				filterMask[1][2].setText("-1");
				filterMask[2][0].setText("-1");
				filterMask[2][1].setText("-1");
				filterMask[2][2].setText("-1");
			} else if (filterType.getSelectedItem().equals("Rozmycie Gaussowskie")) {
				filterMask[0][0].setText("1");
				filterMask[0][1].setText("2");
				filterMask[0][2].setText("1");
				filterMask[1][0].setText("2");
				filterMask[1][1].setText("4");
				filterMask[1][2].setText("2");
				filterMask[2][0].setText("1");
				filterMask[2][1].setText("2");
				filterMask[2][2].setText("1");
			} else if (filterType.getSelectedItem().equals("Splot maski")) {
				filterMask[0][0].setText("0");
				filterMask[0][1].setText("0");
				filterMask[0][2].setText("0");
				filterMask[1][0].setText("0");
				filterMask[1][1].setText("0");
				filterMask[1][2].setText("0");
				filterMask[2][0].setText("0");
				filterMask[2][1].setText("0");
				filterMask[2][2].setText("0");
			}

		} else if (e.getSource() == colorOK) {
			// metoda liczaca %% kolorów
			if (selectColor.getSelectedItem().equals("Red")) {
				howManyColor(0);
			}
			if (selectColor.getSelectedItem().equals("Green")) {
				howManyColor(1);
			}
			if (selectColor.getSelectedItem().equals("Blue")) {
				howManyColor(2);
			}

			skladowaColorLicznik = skladowaColorLicznik * 100;
			float wynik = skladowaColorLicznik / colorLicznik;

			RGBcolorLicznik = RGBcolorLicznik * 100;
			float wynikRBG = RGBcolorLicznik / sumaColorow;

			colorWynik.setText("Wynik: Sk³adowa o numerze " + (int) skladowaCheck + " stanowi"
					+ String.format("%.2f", wynik) + "% koloru " + selectColor.getSelectedItem() + ", natomiast kolor "
					+ selectColor.getSelectedItem() + " stawnowi " + String.format("%.2f", wynikRBG) + "% koloru "
					+ selectColor.getSelectedItem() + " na obrazie");
		} else if (e.getSource() == miShowHistogram) {
			histogram.calculateHistogram(actualImage);
			histogram.repaint();
			histogram.setVisible(true);
		} else if (e.getSource() == histogramStreatch) {
			histogramStreatching(Integer.parseInt(jtfVMin.getText()), Integer.parseInt(jtfVMax.getText()));
		} else if (e.getSource() == miHistogramEqualization) {
			histogramEqualization();
		} else if (e.getSource() == binBut) {
			threshold(Integer.parseInt(jtfTr.getText()));
		} else if (e.getSource() == miBinaryzacja) {
			otsuThreshold();
		} else if (e.getSource() == binNBut) {
			niblackThreshold(Integer.parseInt(jtfRozmiarOkna.getText()), Double.parseDouble(jtfProg.getText()));
		}

	}

	// SLIDERY-> do zadania numer 4, oglnie to zadanie siê zmieni³o, wczeœniej
	// by³ kontrast wiêc, ju¿ tak zostanie
	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == zoomSlider) {
			actualImageScale.setText("Rozmiar obrazu: " + zoomSlider.getValue() + "%");
		} else if (e.getSource() == brightnessSlider) {
			brightness.setText("Jasnoœæ: " + brightnessSlider.getValue() + "%");
		} else if (e.getSource() == kontrastSlider) {
			kontrast.setText("Kontrast: " + kontrastSlider.getValue() + "%");
		} else if (aktualnaPozycja == -1) {
			return;
		}

		punkty[aktualnaPozycja] = e.getPoint();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		// pokazywanie pixela po lewej stronie naszego panelu
		Color pixelColor = robot.getPixelColor(e.getXOnScreen(), e.getYOnScreen());
		colorRGB.setText("Kolor RGB: [" + Integer.toString(pixelColor.getRed()) + " "
				+ Integer.toString(pixelColor.getGreen()) + " " + Integer.toString(pixelColor.getBlue()) + "]");
		colorPreview.setBackground(pixelColor);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == imagePane & id != 4) {
			licznik++;
			pInvert = new Point(e.getX(), e.getY());
			licznik++;

			if (licznik == 3) {
				reset();
			}

			// je¿eli mamy obraz wczytany w panel to robimy histogram
			if (actualImage != null) {
				histogram.calculateHistogram(actualImage);
				histogram.repaint();
			}
			imagePane.repaint();
			licznik = 1;

		}
		// obliczanie metod gryscale, zadanie4 i tak dalej np. dodawna(),
		// odejmowana()
		if (e.getSource() == grayOne) {
			greyscale();
		}
		if (e.getSource() == grayTwo) {
			greyscaleTwo();
		}
		if (e.getSource() == buttons[3]) {
			dodana();
		}
		if (e.getSource() == buttons[4]) {
			odejmowana();
		}
		if (e.getSource() == buttons[5]) {
			mno¿ona();
		}
		if (e.getSource() == buttons[6]) {
			dzielena();

		}

		if (e.getSource() == skaluj) {
			// POPRAWIÆ
			getpozycjaSkalowanie();
			double wartoscSkalowania = 1;
			System.out.println(wartoscSkalowania);
			punkty[0] = new Point2D.Double(
					skalowanyX1Check * wartoscSkalowania + (1 - wartoscSkalowania) * skalowanyX1Check,
					skalowanyY1Check * wartoscSkalowania + (1 - wartoscSkalowania) * skalowanyY1Check);
			punkty[1] = new Point2D.Double(
					skalowanyX2Check * wartoscSkalowania + (1 - wartoscSkalowania) * skalowanyX2Check,
					skalowanyY2Check * wartoscSkalowania + (1 - wartoscSkalowania) * skalowanyY2Check);

			punkty[aktualnaPozycja] = e.getPoint();
			repaint();
			// malowanie

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// POPRAWIÆ
		Point punkt = e.getPoint();
		for (int i = 0; i < punkty.length; i++) {

			double x = punkty[i].getX() - 10;
			double y = punkty[i].getY() - 10;

			Rectangle2D r = new Rectangle2D.Double(x, y, 20, 20);
			if (r.contains(punkt)) {
				aktualnaPozycja = i;
				return;
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == zoomSlider) {
			resize((double) zoomSlider.getValue() / 100);
			imagePane.setPreferredSize(new Dimension((int) ((double) iWidth * zoomSlider.getValue() / 100),
					(int) ((double) iHeight * zoomSlider.getValue() / 100)));

			imagePane.repaint();
		} else if (e.getSource() == brightnessSlider && actualImage != null) {
			brightness((double) brightnessSlider.getValue() / 100);
			imagePane.repaint();
		} else if (e.getSource() == kontrastSlider && actualImage != null) {
			kontrast((double) kontrastSlider.getValue() / 100);
			imagePane.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent evt) {
		// do pól tekstowych mozna wpisaæ tylko liczby, bez ¿anych znaków
		char c = evt.getKeyChar();
		if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
			evt.consume();
		}
		;
	}
}
