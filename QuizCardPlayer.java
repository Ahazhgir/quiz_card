import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {
	
	private JTextArea display;
	private ArrayList<QuizCard> cardList;
	private QuizCard currentCard;
	private int currentCardIndex;
	private JFrame frame;
	private JButton nextButton;
	private boolean isShowAnswer;

	public static void main(String[] args) {
		QuizCardPlayer reader = new QuizCardPlayer();
		reader.go();

	}
	
	public void go() {
		//формируем GUI
		
		frame = new JFrame("Викторина");
		JPanel mainPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD, 24);
		
		display = new JTextArea(12, 25);
		display.setFont(bigFont);
		display.setLineWrap(true);
		display.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(display);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		nextButton = new JButton("Загрузите карточки через меню \"Файл\"");
		nextButton.setEnabled(false);
		mainPanel.add(qScroller);
		buttonPanel.add(nextButton);
		nextButton.addActionListener(new NextCardListener());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Файл");
		JMenuItem loadMenuItem = new JMenuItem("Загрузить набор карточек");
		loadMenuItem.addActionListener(new OpenMenuListener());
		fileMenu.add(loadMenuItem);
		menuBar.add(fileMenu);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		frame.setSize(640, 500);
		frame.setVisible(true);
		
	}
	
	public class NextCardListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(isShowAnswer) {
				//показываем ответ, так как вопрос уже был увиден
				display.setText(currentCard.getAnswer());
				nextButton.setText("Следующая карточка");
				isShowAnswer = false;
			} else {
				//Показываем следующий вопрос
				if(currentCardIndex < cardList.size()) {
					showNextCard();
				} else {
					//Карточек больше нет
					display.setText("Это была последняя карточка");
					nextButton.setEnabled(false);
				}
			}
			
		}
		
	}
	
	public class OpenMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			loadFile(fileOpen.getSelectedFile());
			
		}
		
	}
	
	private void loadFile(File file) {
		cardList = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				makeCard(line);
			}
			reader.close();
		} catch(Exception e) {
			System.out.println("Не получилось прочитать карточки из файла");
			e.printStackTrace();
		}
		showNextCard();
	}
	
	private void makeCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		cardList.add(card);
		System.out.println("Считали карточку");
	}
	
	private void showNextCard() {
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		display.setText(currentCard.getQuestion());
		nextButton.setText("Показать ответ");
		nextButton.setEnabled(true);
		isShowAnswer = true;
	}

}
