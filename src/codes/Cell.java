package codes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

//import codes.Cell.MyMouseListener;

public class Cell extends JPanel{
  	private int status;
  	public int MINESTEP = 'X';
  	public int MARKED = 'P';
  	public int UNKNOWN = '_';
  	public int FALSEMARK = 'F';
  	public int SAFE = 0;
  	public int MINE = 1;
	
	public Cell() {
		setBorder(new LineBorder(Color.black, 1));
		status = UNKNOWN;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("monospaced", Font.BOLD, 28));

		if (status == '0') {
			setBackground(Color.LIGHT_GRAY);
		} else if (status >= '1' && status <= '8') {
			String mineNum = " ";
			setBackground(Color.LIGHT_GRAY);
			if (status == '1') {
				g.setColor(Color.BLUE);
				mineNum = "1";
			} else if (status == '2') {
				g2.setColor(Color.GREEN);
				mineNum = "2";
			} else if (status == '3') {
				g2.setColor(Color.MAGENTA);
				mineNum = "3";
			} else if (status == '4') {
				g2.setColor(Color.ORANGE);
				mineNum = "4";
			} else if (status == '5') {
				g2.setColor(Color.PINK);
				mineNum = "5";
			} else if (status == '6') {
				g2.setColor(Color.CYAN);
				mineNum = "6";
			} else if (status == '7') {
				g2.setColor(Color.YELLOW);
				mineNum = "7";
			} else if (status == '8') {
				g2.setColor(new Color(100, 100, 100));
				mineNum = "8";
			}
			int stringWidth = g.getFontMetrics().stringWidth(mineNum);
			int stringHeight = g.getFontMetrics().getHeight();
			g.drawString(mineNum, (getWidth()-stringWidth)/2, (getHeight()-stringHeight)/2 + g.getFontMetrics().getAscent());
		} else if (status == MINESTEP) {
				g2.setColor(Color.BLACK);
				g2.drawLine(8, 8, getWidth()-8, getHeight()-8);
				g2.drawLine(8, getHeight()-8, getWidth()-8, 8);
				g2.drawLine(5, getHeight()/2, getWidth()-5, getHeight()/2);
				g2.drawLine(getWidth()/2, 5, getWidth()/2, getHeight()-5);
				g2.fillOval(9, 9, getWidth()-18, getHeight()-18);
		} else {
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, getWidth()/6, getHeight());
			g2.fillRect(0, 0, getWidth(), getHeight()/6);
			g2.setColor(Color.GRAY);
			g2.fillRect(getWidth()-getWidth()/7, getHeight()/6, getWidth()/7, getHeight() - getHeight()/6);
			g2.fillRect(getWidth()/6, getHeight()-getHeight()/7, getWidth() - getWidth()/6, getHeight()/7);
			
			int[] x1 = {0, getWidth()/6, getWidth()/6};
			int[] y1 = {getHeight(), getHeight(), getHeight()-getHeight()/7};
			g2.fillPolygon(x1, y1, 3);
			
			int[] x2 = {getWidth(), getWidth(), getWidth()-getWidth()/7};
			int[] y2 = {0, getHeight()/6, getHeight()/6};
			g2.fillPolygon(x2, y2, 3);
			
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(getWidth()/6, getHeight()/6, getWidth() - getWidth()/6 - getWidth()/7, getHeight() - getHeight()/7 - getHeight()/7);
			if (status == MARKED) {
				g2.setColor(Color.RED);
				int stringWidth = g.getFontMetrics().stringWidth("M");
				int stringHeight = g.getFontMetrics().getHeight();
				g2.drawString("M", (getWidth()-stringWidth)/2, (getHeight()-stringHeight)/2 + g.getFontMetrics().getAscent());
			} else if (status == FALSEMARK) {
				g2.setColor(Color.RED);
				int stringWidth = g.getFontMetrics().stringWidth("M");
				int stringHeight = g.getFontMetrics().getHeight();
				g2.drawString("M", (getWidth()-stringWidth)/2, (getHeight()-stringHeight)/2 + g.getFontMetrics().getAscent());
				g2.setColor(Color.BLACK);
				g2.drawLine(5, 5, getWidth()-5, getHeight()-5);
				g2.drawLine(5, getHeight()-5, getWidth()-5, 5);
			}
		}
	}
}
