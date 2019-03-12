import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Font;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputEvent;

public class Linia extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

	private JMenuBar menuBar;
	private JMenu mPlik, mObraz, mOkno;
	private JMenuItem miOpenImage, miSaveImage, miResetImage, miExit;
	private JPanel contentPane, configurationPane, imagePane, dimentionsOblong, dimentionsCircle, dimentionsLine;
	private JFileChooser chooser, saveChooser;
	private JLabel colorRGB, colorPreview, actualImageScale, brightness;
	private JScrollPane scrollImagePane;
	private JSlider zoomSlider, brightnessSlider;
	private BufferedImage actualImage, originalImage;
	int positionX, positionY;
	int oHeightCheck = 0, oWidthCheck = 0, circleRCheck, lineXCheck = 0, lineYCheck = 0;
	private JLabel label1, label2, label3, label4, label5;
	private JTextField oblongHeight, oblondWidth, circleR, lineX, lineY;
	private JButton[] buttons = new JButton[3];
	int id = 0;
	boolean isSelected = false, isAutomatic = true;

	private Robot robot;
	private Point pInvert = null;
	private int iWidth, iHeight;

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

		setJMenuBar(menuBar);

		configurationPane = new JPanel();
		configurationPane.setBackground(Color.WHITE);
		configurationPane.setBounds(0, 0, 300, 700);
		configurationPane.setLayout(null);
		contentPane.add(configurationPane);

		zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 1350, 1);
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

		// PANEL GDZIE BEDZIE NASZ OBRAZ
		imagePane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				try {
					// TERAZ DDZIA£AMY NA PIXELACH, bierzemy punkt i malujemy na
					// bia³o
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

						// id=1 ¿e jesteœmy na panelu malowania kwadratu
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

						// id=1 ¿e jesteœmy na panelu malowania okrêgu
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
		// dodanie pasków do skrolowania
		scrollImagePane = new JScrollPane(imagePane);
		scrollImagePane.setPreferredSize(new Dimension(1045, 650));
		scrollImagePane.setBorder(null);
		scrollImagePane.setBounds(300, 0, 1045, 650);
		scrollImagePane.setAutoscrolls(true);
		contentPane.add(scrollImagePane);
		// do zadania nastepnego, ale ¿e ja maluje na obrazch to teraz jest to
		// zrobion(PPN metodê siê dorobi);
		// otwoeranie okna z naszymi obrazami w formatach takich jak poni¿ej
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "gif", "png");
		chooser.setFileFilter(filter);
		saveChooser = new JFileChooser();
		// do podgl¹du jak nasz "robot" pobiera pixele, w danym momencie
		colorRGB = new JLabel("Kolor RGB: ---");
		colorRGB.setFont(new Font("Arial", Font.PLAIN, 12));
		colorRGB.setBounds(75, 25, 250, 40);
		configurationPane.add(colorRGB);
		// podgl¹d koloru na którym znajuje siê kursor
		colorPreview = new JLabel();
		colorPreview.setBounds(25, 25, 40, 40);
		colorPreview.setOpaque(true);
		colorPreview.setBackground(Color.WHITE);
		configurationPane.add(colorPreview);
		// do skalowania obrazem
		actualImageScale = new JLabel("Rozmiar obrazu: 100%");
		actualImageScale.setFont(new Font("Arial", Font.PLAIN, 12));
		actualImageScale.setBounds(25, 85, 250, 12);
		configurationPane.add(actualImageScale);
		// jasnosæ do nastpenych zadañ
		brightness = new JLabel("Jasnoœæ: 100%");
		brightness.setFont(new Font("Arial", Font.PLAIN, 12));
		brightness.setBounds(25, 145, 250, 40);
		configurationPane.add(brightness);
		// slajder którym ustawiamy jasnoœæ, ciemnoœæ w kolejnych zadaniech
		// bedzie podobnie
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
		// tablica przycisków naszych elementów
		buttons[0] = new JButton("LINIA");
		buttons[0].setFont(new Font("Tahoma", Font.PLAIN, 10));
		buttons[0].setBorderPainted(false);
		buttons[0].setBackground(Color.ORANGE);
		buttons[0].setBounds(15, 250, 70, 30);
		buttons[0].addActionListener(this);
		configurationPane.add(buttons[0]);

		buttons[1] = new JButton("PROSTOK¥T");
		buttons[1].setFont(new Font("Tahoma", Font.PLAIN, 10));
		buttons[1].setBorderPainted(false);
		buttons[1].setBackground(Color.ORANGE);
		buttons[1].setBounds(90, 250, 110, 30);
		buttons[1].addActionListener(this);
		configurationPane.add(buttons[1]);

		buttons[2] = new JButton("OK¥G");
		buttons[2].setFont(new Font("Tahoma", Font.PLAIN, 10));
		buttons[2].setBorderPainted(false);
		buttons[2].setBackground(Color.ORANGE);
		buttons[2].setBounds(205, 250, 70, 30);
		buttons[2].addActionListener(this);
		configurationPane.add(buttons[2]);
		// panele, póiej ustawiane sa na ID, i pokazywne, tak jak wczêœniej to
		// by³o pokazane
		dimentionsOblong = new JPanel();
		dimentionsOblong.setBounds(0, 280, 300, 600);
		dimentionsOblong.setLayout(null);
		dimentionsOblong.setVisible(true);
		dimentionsOblong.setBackground(Color.ORANGE);
		configurationPane.add(dimentionsOblong);

		dimentionsCircle = new JPanel();
		dimentionsCircle.setBounds(0, 280, 300, 600);
		dimentionsCircle.setLayout(null);
		configurationPane.add(dimentionsCircle);
		dimentionsCircle.setVisible(true);
		dimentionsCircle.setBackground(Color.ORANGE);

		dimentionsLine = new JPanel();
		dimentionsLine.setBounds(0, 280, 300, 600);
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

		label5 = new JLabel("Punkt pocz¹tkowy Y:");
		label5.setBounds(20, 85, 150, 40);
		label5.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dimentionsLine.add(label5);

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ******************************************************************************************************************************//
	// KONIEC TOWORZENIA WYGLADU: POCZ¥TEK METOD //
	// ******************************************************************************************************************************//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// poberanie pozycji gdzie siê myszka znajduje
	public int getThisX(MouseEvent e) {
		return e.getX();
	}

	public int getThisY(MouseEvent e) {
		return e.getY();
	}

	// pobieranie pozycji informacyjnie dla mnie
	private void getPositionInformation(MouseEvent e) {
		positionX = e.getX();
		positionY = e.getY();
		System.out.println("Pozycja x: " + positionX + ", y: " + positionY);
	}

	// pobieranie pozycji do prostok¹tu
	private void getValues() {
		oHeightCheck = Integer.parseInt(oblongHeight.getText());
		oWidthCheck = Integer.parseInt(oblondWidth.getText());
		System.out.println(oHeightCheck + " " + oWidthCheck);
	}

	// pobieranie pozycji do lini
	private void getLineStartPosition() {
		lineYCheck = Integer.parseInt(lineY.getText());
		lineXCheck = Integer.parseInt(lineX.getText());
		System.out.println(lineXCheck + " " + lineYCheck);
	}

	// pobieranie z textFielda naszej œrednicy, podobnie jak wy¿ej
	private void getR() {
		circleRCheck = Integer.parseInt(circleR.getText());
	}

	// metoda tworz¹ca linie, pixel po pixelu
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

	// metoda robiaca okregi
	public void CirclePolar(Color c, int xc, int yc, int r) {
		int color = c.getRGB();
		double x, y;
		for (double i = 1.0 / r; i <= 2 * Math.PI; i += 1.0 / r) {
			y = yc + r * Math.sin(i);
			x = xc + r * Math.cos(i);
			actualImage.setRGB((int) x, (int) y, color);
		}
	}

	// metoda tworz¹ca prostok¹t
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

	// drozjaœnianie, do kolejnych zadañ
	public void brightness(double brightnessValue) {
		int[] LUT = new int[256];

		for (int i = 0; i < 256; i++) {
			double newValue = i * brightnessValue;
			if (newValue > 255)
				LUT[i] = 255;
			else if (newValue < 0)
				LUT[i] = 0;
			else
				LUT[i] = (int) newValue;
		}

		updateImage(LUT);
	}

	// metoda do kolejnych zadañ, potrzebne, jak bedziemy robic mnonie i
	// rozjaœninie, zmienianie ca³ego obrazu pixel po pixelu
	private void updateImage(int[] LUT) {
		int[] pixel = new int[4];
		for (int i = 0; i < actualImage.getWidth(); i++) {
			for (int j = 0; j < actualImage.getHeight(); j++) {
				pixel = actualImage.getRaster().getPixel(i, j, new int[4]);

				actualImage.getRaster().setPixels(i, j, 1, 1,
						new int[] { LUT[pixel[0]], LUT[pixel[1]], LUT[pixel[2]], LUT[pixel[3]] });
			}
		}
		imagePane.repaint();
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miOpenImage) {
			int returnVal = chooser.showOpenDialog(configurationPane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					actualImage = ImageIO.read(chooser.getSelectedFile());
					originalImage = ImageIO.read(chooser.getSelectedFile());
				} catch (IOException f) {
					f.printStackTrace();
				}

				iWidth = actualImage.getWidth();
				iHeight = actualImage.getHeight();
				imagePane.setPreferredSize(new Dimension(actualImage.getWidth() * zoomSlider.getValue() / 100,
						actualImage.getHeight() * zoomSlider.getValue() / 100));
				imagePane.revalidate();
				imagePane.repaint();
			}
		} else if (e.getSource() == miSaveImage) {
			int returnVal = saveChooser.showSaveDialog(configurationPane);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					ImageIO.write(actualImage, "PNG", new File(saveChooser.getSelectedFile() + ".png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == miResetImage) {
			actualImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = actualImage.createGraphics();
			g.drawImage(originalImage, 0, 0, null);

			zoomSlider.setValue(100);
			brightnessSlider.setValue(100);
			imagePane.repaint();
		}

		// kolorowanie naszego panelu w zale¿nosæi na którym siê teraz
		// znajdujemy
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
		}

	}

	// do kolejnych zadañ
	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == zoomSlider) {
			actualImageScale.setText("Rozmiar obrazu: " + zoomSlider.getValue() + "%");
		} else if (e.getSource() == imagePane) {
			pInvert = new Point(e.getX(), e.getY());
			imagePane.repaint();
		} else if (e.getSource() == brightnessSlider) {
			brightness.setText("Jasnoœæ: " + brightnessSlider.getValue() + "%");
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println(e.getX() + " " + e.getY());
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		// pobieranie naszego pixela, i pokazywany potem jest po lewej stronie w
		// panelu
		Color pixelColor = robot.getPixelColor(e.getXOnScreen(), e.getYOnScreen());
		colorRGB.setText("Kolor RGB: [" + Integer.toString(pixelColor.getRed()) + " "
				+ Integer.toString(pixelColor.getGreen()) + " " + Integer.toString(pixelColor.getBlue()) + "]");
		colorPreview.setBackground(pixelColor);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Graphics g = imagePane.getGraphics();

		if (id == 2 & isAutomatic == false) {
			System.out.println("maluj prostok¹t z wymiarów");
			try {
				getValues();
				g.drawRect(e.getX(), e.getY(), oWidthCheck, oHeightCheck);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(imagePane, "Proszê wpisaæ liczbê!");
			}
		}
		if (id == 2 & isAutomatic == true) {
			System.out.println("maluj prostok¹t samodzielnie");
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
		// walidacja danych-> brane tylko liczby
		char c = evt.getKeyChar();
		if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
			evt.consume();
		}
		;
	}
}
