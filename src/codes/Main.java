package codes;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import codes.Board;
import java.awt.*;

public class Main {
	
	public static int prob = 0;
	
	public static void main(String[] args) {
	    
		int width = 20;
		int height = 10;
		JFrame window = new JFrame();
		
		JLabel setting = new JLabel("Please select the level of difficulty");
		setting.setBorder(new LineBorder(Color.yellow, 2));
		window.add(setting, BorderLayout.SOUTH);
		
		String[] difficulty = {"easy", "medium", "hard"};
		int[] mineProb = {15, 25, 40};
			
		JList list = new JList(difficulty);
		list.setVisibleRowCount(3);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		window.add(new JScrollPane(list));
		
		window.setSize(500, 500);
		window.setTitle("Minesweeper");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		list.addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						prob = mineProb[list.getSelectedIndex()];
						window.dispose();
						
						int numOfMine = width*height*prob/100;
						Board board = new Board(width, height, numOfMine, prob);
						JFrame map = board.new Map(width, height);
						map.setSize(width*50, height*50);
						map.setTitle("Minesweeper");
						map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						map.setVisible(true);
						board.printBoard(width, height);
					}
				}
				);
	}
}
