package codes;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Board extends JFrame {
	public int MINE = 1;
	public int SAFE = 0;
	public boolean gameOver = false;
	public int mineLeft = 0;
	private Cell[][] boardCell;
	
	// randomStatus(min, max): returns a random number between min and max; 
	// used for distributing mines on the Minesweeper board
	public double randomStatus(int min, int max) {
		double random = Math.random()*(max-min+1) + min;
		return random;
	}
	
	// Board (width, height, numOfMines, mineProb): constructs the Minesweeper board of dimensions width and height 
	// with a total of numOfMines mines; each cell contains a status of either SAFE or MINE and mineProb
	// is used in random generation of mines to ensure that mines are distributed more evenly
	public Board(int width, int height, int numOfMines, int mineProb) {
		boardCell = new Cell[height][width];
		mineLeft = numOfMines;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				boardCell[r][c] = new Cell();
				double randomProb = randomStatus(0, 100/mineProb);
				if (height*width - c - width*r <= numOfMines && numOfMines > 0) {
					// set status to be MINE if the number of cell left <= number of mines to be distributed
					(boardCell[r][c]).setStatus(MINE);
					numOfMines--;
				} else if (randomProb >= 1.00 && randomProb <= 2.00 && numOfMines > 0) {
					// set status to MINE according to random number generation
					(boardCell[r][c]).setStatus(MINE);
					numOfMines--;
				} else {
					(boardCell[r][c]).setStatus(SAFE);
				}
			}
		}
	}
	
	// printBoard(width, height): prints the board on the console so that the programmer can see the
	// status of each cell; used for testing purposes
	public void printBoard(int width, int height) {
		for (int r = 0; r < height; ++r) {
			for (int c = 0; c < width; ++c) {
				if (boardCell[r][c].getStatus() == MINE) {
					System.out.print(MINE);
				} else if (boardCell[r][c].getStatus() == SAFE) {
					System.out.print(SAFE);
				}

				if (c != width - 1) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}  
	}
	
	public class Map extends JFrame {
		public int MINESTEP = 'X';
		public int UNKNOWN = '_';
		public int MARKED = 'P';
		public int FALSEMARK = 'F';
		public int allCellWidth = 0;
		public int allCellHeight = 0;
		private Cell[][] mapCell;
		public int mapWidth = 0;
		public int mapHeight = 0;
		final public int thickness = 2;
		
		// creating a JLabel that tells users the status of the game
		JLabel gameStatus = new JLabel(String.format("There are a total of %d mines", mineLeft));
		JPanel panel;
		
		// Map(width, height): constructs a Minesweeper map with dimensions width and height; 
		// each cell contain a status of UNKNOWN at the beginning of the game which means that
		// each cell is unrevealed. When the user clicks on a cell, its status changes accordingly
		// to either digits 1-9, MARKED, MINESTEP, FALSEMARK, or remains UNKNOWN
		public Map (int width, int height) {
			panel = new JPanel(new GridLayout(height, width, thickness, thickness));
			mapCell = new Cell[height][width];	
			mapWidth = width;
			mapHeight = height;
			for (int r = 0; r < height; r++) {
				for (int c = 0; c < width; c++) {
					mapCell[r][c] = new Cell();
					mapCell[r][c].setStatus(UNKNOWN);
					panel.add(mapCell[r][c]);
				}
			}
			// adding the mouse listener to the panel
			panel.addMouseListener(new Mouseclass());
			panel.setBorder(new LineBorder(Color.black, 2));
			panel.setBackground(Color.BLACK);
			gameStatus.setBorder(new LineBorder(Color.yellow, 2));

			add(panel, BorderLayout.CENTER);
			add(gameStatus, BorderLayout.SOUTH);
		}
		
		// A class that deals with mouse events
		public class Mouseclass extends MouseAdapter {
			// mouseClicked(event): called whenever the user clicks the mouse; behaves according
			// to whether the user right-clicked or left-clicked, and whether he/she clicked once or twice
			public void mouseClicked(MouseEvent event) {
				if (gameOver) {
					return;
				}
				
				double cellWidth = panel.getComponentAt(event.getX(), event.getY()).getSize().getWidth();
				double cellHeight = panel.getComponentAt(event.getX(), event.getY()).getSize().getHeight();
				
				// user clicked at the border and not on the cell
				if ((int) cellWidth == panel.getWidth() || (int) cellHeight == panel.getHeight()) {
					return;
				}
				
				double alignLeft =  panel.getComponent(0).getBounds().getLocation().getX();
				double alignTop = panel.getComponent(0).getBounds().getLocation().getY();
				
				int columnNum = (int) ((event.getX()-alignLeft) / (cellWidth+thickness));
				int rowNum = (int) ((event.getY()-alignTop) / (cellHeight+thickness));
				allCellWidth = (int) cellWidth;
				allCellHeight = (int) cellHeight;
				
				if (event.isMetaDown()) { //right clicking
					mark(rowNum, columnNum, mapWidth, mapHeight);
				} else {
					int mine = 0;
					
					if (event.getClickCount() == 1) { //clicked once
						mine = step(rowNum, columnNum, mapWidth, mapHeight);
					} else if (event.getClickCount() >= 2 && 
							mapCell[rowNum][columnNum].getStatus() >= '1' && 
							mapCell[rowNum][columnNum].getStatus() <= '8'){
						mine = stepAdv(rowNum, columnNum, mapWidth, mapHeight);
					} else {
						return;
					}
					
					if (mine == -1) {
						gameStatus.setText("You stepped on a mine and lost the game!");
						gameOver = true;
						if (mapCell[rowNum][columnNum].getStatus() == MINESTEP) {
							mapCell[rowNum][columnNum].setBackground(Color.RED);
						}
						stepAll(mapWidth, mapHeight);
						
					} else if (mine == 1) {
						gameStatus.setText("You successfully stepped on all safe squares and won the game!");
						gameOver = true;
						markAll(mapWidth, mapHeight);
					}
				}
			}
		}
		
		// mineAt(r, c, width, height): returns the number of mines at row r and column c
		public boolean mineAt(int r, int c, int width, int height) {
			if (c >= width || c < 0 || r >= height || r < 0) {
				return false;
			}
			
			if (boardCell[r][c].getStatus() == MINE) {
				return true;
			}
			return false;
		}
		
		// stepAll(width, height): step on all cells on the board; called after the user
		// clicked on a mine and lost the game
		public void stepAll (int width, int height) {
			for (int r = 0; r < height; r++) {
				for (int c = 0; c < width; c++) {
					if (mapCell[r][c].getStatus() == MARKED && boardCell[r][c].getStatus() == MINE) {
						continue;
					}
					if (mapCell[r][c].getStatus() == MARKED && boardCell[r][c].getStatus() == SAFE) {
						mapCell[r][c].setStatus(FALSEMARK);
						continue;
					}
					
					boolean mine = mineAt(r, c, width, height);
					if (mine == true) {
						mapCell[r][c].setStatus(MINESTEP);
					} else {
						int mineTotal = 0;
						if (mineAt(r-1, c-1, width, height)) {
							++mineTotal;
						}
						if (mineAt(r, c-1, width, height)) {
							++mineTotal;
						}
					    if (mineAt(r+1, c-1, width, height)) {
					    	++mineTotal;
					    }
						if (mineAt(r-1, c, width, height)) {
							++mineTotal;
						}
						if (mineAt(r+1, c, width, height)) {
							++mineTotal;
						}
					    if (mineAt(r-1, c+1, width, height)) {
					    	++mineTotal;
					    }
						if (mineAt(r, c+1, width, height)) {
							++mineTotal;
						}
						if (mineAt(r+1, c+1, width, height)) {
							++mineTotal;
						}
						mapCell[r][c].setStatus(mineTotal + '0');
					}
				}
			}
		}
		
		// step(r, c, width, height): steps on the cell at row r and column c and reveal its status; 
		// also steps on all adjacent cells if there are no mines in adjacent cells; user steps on a cell by left-clicking once
		public int step(int r, int c, int width, int height) {
			if (c >= width || c < 0 || r >= height || r < 0) {
				return 0;
			}
			if ((mapCell[r][c].getStatus() >= '0' &&  mapCell[r][c].getStatus() <= '8') 
					|| mapCell[r][c].getStatus() == MARKED) {
				return 0;
			}
			
			boolean all = allStepped(width, height);
			if (all) {
				return 1;
			}
			
			boolean mine = mineAt(r, c, width, height);

			if (mine == true) {
				mapCell[r][c].setStatus(MINESTEP);
				return -1;
			} else {
				int mineTotal = 0;
				if (mineAt(r-1, c-1, width, height)) {
					++mineTotal;
				}
				if (mineAt(r, c-1, width, height)) {
					++mineTotal;
				}
			    if (mineAt(r+1, c-1, width, height)) {
			    	++mineTotal;
			    }
				if (mineAt(r-1, c, width, height)) {
					++mineTotal;
				}
				if (mineAt(r+1, c, width, height)) {
					++mineTotal;
				}
			    if (mineAt(r-1, c+1, width, height)) {
			    	++mineTotal;
			    }
				if (mineAt(r, c+1, width, height)) {
					++mineTotal;
				}
				if (mineAt(r+1, c+1, width, height)) {
					++mineTotal;
				}

				mapCell[r][c].setStatus(mineTotal + '0');
				
				if (mapCell[r][c].getStatus() == '0') {
					step(r-1, c-1, width, height);
					step(r, c-1, width, height);
					step(r+1, c-1, width, height);
					step(r-1, c, width, height);
					step(r+1, c, width, height);
					step(r-1, c+1, width, height);
					step(r, c+1, width, height);
					step(r+1, c+1, width, height);
				}
				if (allStepped(width, height)) {
					return 1;
				}
			}
			return 0;
		}
		
		// markAll(width, height): mark all unrevealed cells on the board; called after
		// the user steps on all safe squares and won the game, but have not marked all squares
		public void markAll(int width, int height) {
			for (int r = 0; r < height; ++r) {
				for (int c = 0; c < width; ++c) {
					if (boardCell[r][c].getStatus() == MINE) {
						mapCell[r][c].setStatus(MARKED);
					}
				}
			}
		}
		
		// allStepped(width, height): determines whether or not every cell on the board is revealed
		public boolean allStepped(int width, int height) {
			for (int r = 0; r < height; ++r) {
				for (int c = 0; c < width; ++c) {
					if ((mapCell[r][c].getStatus() < '0' || mapCell[r][c].getStatus() > '8') && boardCell[r][c].getStatus() == SAFE) {
						return false;
					}
				}
			}
			return true;
		}
		
		// mark(r, c, width, height): mark the cell at row r and column c; user marks the cell by right clicking
		public void mark(int r, int c, int width, int height) {
			if (c >= width || r >= height || c < 0 || r < 0) {
				return;
			} 

			if (mapCell[r][c].getStatus() == MARKED) {
				mapCell[r][c].setStatus(UNKNOWN);
				mineLeft++;
			} else if (mapCell[r][c].getStatus() == UNKNOWN && mineLeft > 0) {
				mapCell[r][c].setStatus(MARKED);
				mineLeft--;
			}
			gameStatus.setText(String.format("You have %d more mines to mark", mineLeft));
		}
		
		// numMarked(r, c, width, height): determine the number of adjacent cells that is marked; 
		// used together with the function stepAdv
		public int numMarked(int r, int c, int width, int height) {
			if (c >= width || r >= height || c < 0 || r < 0) {
				return 0;
			} 
			
			int n = 0;
			if ((r > 0 && c > 0) && mapCell[r-1][c-1].getStatus() == MARKED) {
				n++;
			} 
			if ((r > 0) && mapCell[r-1][c].getStatus() == MARKED) {
				n++;
			} 
			if ((r > 0 && c < width - 1) && mapCell[r-1][c+1].getStatus() == MARKED) {
				n++;
			} 
			if (c > 0 && mapCell[r][c-1].getStatus() == MARKED) {
				n++;
			}
			if (c < width - 1 && mapCell[r][c+1].getStatus() == MARKED) {
				n++;
			} 
			if (r < height - 1 && c > 0 && mapCell[r+1][c-1].getStatus() == MARKED) {
				n++;
			} 
			if (r < height - 1 && mapCell[r+1][c].getStatus() == MARKED) {
				n++;
			} 
			if (r < height - 1 && c < width - 1 && mapCell[r+1][c+1].getStatus() == MARKED) {
				n++;
			}
			return n;
		}
		
		// stepAdv(r, c, width, height): steps on all unmarked adjacent cells given that the number of 
		// marked adjacent cells is equal to the digit on the current cell; called when user left-clicked twice on a cell
		public int stepAdv(int r, int c, int width, int height) {
			int n = numMarked(r, c, width, height);
			int mine1 = 0, mine2 = 0, mine3 = 0, mine4 = 0;
			int mine5 = 0, mine6 = 0, mine7 = 0, mine8 = 0;
			if (n == (mapCell[r][c].getStatus() - '0')) {
				mine1 = step(r-1, c-1, width, height);
				mine2 = step(r, c-1, width, height);
				mine3 = step(r+1, c-1, width, height);
				mine4 = step(r-1, c, width, height);
				mine5 = step(r+1, c, width, height);
				mine6 = step(r-1, c+1, width, height);
				mine7 = step(r, c+1, width, height);
				mine8 = step(r+1, c+1, width, height);
			}
			if (mine1 == -1 || mine2 == -1 || mine3 == -1 || mine4 == -1 
					|| mine5 == -1 || mine6 == -1 || mine7 == -1 || mine8 == -1) {
				return -1;
			}
			if (mine1 == 1 || mine2 == 1 || mine3 == 1 || mine4 == 1 
					|| mine5 == 1 || mine6 == 1 || mine7 == 1 || mine8 == 1) {
				return 1;
			}
			return 0;
		}
		
		// printMap(width, height): prints the status of each cell on the Map; used for testing purposes
		public void printMap(int width, int height) {
			for (int r = 0; r < height; ++r) {
				for (int c = 0; c < width; ++c) {
					if (mapCell[r][c].getStatus() == UNKNOWN) {
						System.out.print("_");
					} else if (mapCell[r][c].getStatus() == MINESTEP) {
						System.out.print("X");
					} else if (mapCell[r][c].getStatus() == MARKED) {
						System.out.print("P");
					} else {
						System.out.print(mapCell[r][c].getStatus() - '0');
					}

					if (c != width - 1) {
						System.out.print(" ");
					}
				}
				System.out.println();
			}  
		}
	}
}
