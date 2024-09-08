package com.hacademy.topwar.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ScreenRectDialog extends JDialog {
	
	private BufferedImage background;
	
	private static final int BORDER_DRAG_THICKNESS = 5;  // 크기 조절 가능한 경계 두께
    private boolean isResizing = false;  // 크기 조절 중인지 여부
    private Point initialClick;  // 마우스 클릭 초기 위치
    private Rectangle initialRect; //마우스 클릭 시 초기 영역 값
    private int cursorType = Cursor.DEFAULT_CURSOR;  // 현재 커서 타입
    
    private MouseAdapter listener = new MouseAdapter() {
    	public void mousePressed(MouseEvent e) {
    		initialClick = e.getPoint();  // 클릭 위치 저장
    		initialRect = getBounds(); //초기 영역 저장
            isResizing = true;  // 커서가 기본이 아니면 크기 조절 시작
            System.out.println("initialClick = " + initialClick);
    	}
    	public void mouseReleased(MouseEvent e) {
    		isResizing = false;  // 마우스 버튼을 놓으면 크기 조절 종료
    	}
    	public void mouseMoved(MouseEvent e) {
    		// 마우스 위치에 따라 커서 타입 결정
            //setCursorType(e.getPoint());
    	}
    	public void mouseDragged(MouseEvent e) {
    		if (isResizing) {
                // 마우스를 드래그할 때 크기 조절(보류)
                // resizeDialog(e);
    			
    			setLocation(e.getXOnScreen()-initialClick.x, e.getYOnScreen()-initialClick.y);
            }
    	}
    	public void mouseWheelMoved(MouseWheelEvent e) {
    		int rotation = e.getWheelRotation();
    		if(rotation < 0 && transparency < 100) {
    			transparency += 5;
    			repaint();
    		}
    		else if(rotation > 0 && transparency > 0) {
    			transparency -= 5;
    			repaint();
    		}
    	}
	};
	
	private KeyAdapter keyAdapter = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE: 
				dispose();
				break;
			}
		}
	};
	
	public ScreenRectDialog(Window parent) {
		super(parent);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setSize(500, 700);
		int x = parent.getX();
		if(parent.getWidth() > this.getWidth()) 
			x += (parent.getWidth() - this.getWidth()) / 2;
		else if(parent.getWidth() < this.getWidth())
			x -= (parent.getWidth() - this.getWidth()) / 2;
		int y = parent.getY();
		if(parent.getHeight() > this.getHeight())
			y += ((parent.getHeight() - this.getHeight()) / 2);
		else if(parent.getHeight() < this.getHeight())
			y -= ((parent.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0.5f));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setLayout(null);
		try {
			String path = System.getProperty("user.dir")+"/images/bg.png";
			background = ImageIO.read(new File(path));
			
			JPanel panel = new JPanel(true) {
				@Override
				public void paint(Graphics g) {
					Graphics2D g2d = (Graphics2D)g;
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)transparency / 100));
					g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
					g2d.dispose();
				}
			};
			this.setContentPane(panel);

			this.addMouseListener(listener);
			this.addMouseMotionListener(listener);
			this.addMouseWheelListener(listener);
			this.addKeyListener(keyAdapter);
			this.setVisible(true);
		}
		catch(Exception e) {}
		
	}
	
	private int transparency = 100;
	
	// 마우스 위치에 따라 커서 타입 설정
    private void setCursorType(Point p) {
        int x = p.x;
        int y = p.y;
        int width = getWidth();
        int height = getHeight();

        // 크기 조절 가능한 경계 안에 마우스가 있는지 확인
        if (x < BORDER_DRAG_THICKNESS && y < BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.NW_RESIZE_CURSOR;  // 왼쪽 위 모서리
        } else if (x > width - BORDER_DRAG_THICKNESS && y < BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.NE_RESIZE_CURSOR;  // 오른쪽 위 모서리
        } else if (x < BORDER_DRAG_THICKNESS && y > height - BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.SW_RESIZE_CURSOR;  // 왼쪽 아래 모서리
        } else if (x > width - BORDER_DRAG_THICKNESS && y > height - BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.SE_RESIZE_CURSOR;  // 오른쪽 아래 모서리
        } else if (x < BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.W_RESIZE_CURSOR;  // 왼쪽
        } else if (x > width - BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.E_RESIZE_CURSOR;  // 오른쪽
        } else if (y < BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.N_RESIZE_CURSOR;  // 위쪽
        } else if (y > height - BORDER_DRAG_THICKNESS) {
            cursorType = Cursor.S_RESIZE_CURSOR;  // 아래쪽
        } else {
            cursorType = Cursor.DEFAULT_CURSOR;  // 기본 커서
        }

        setCursor(Cursor.getPredefinedCursor(cursorType));
    }

    // 다이얼로그 크기 조절
    private void resizeDialog(MouseEvent e) {
        int x = e.getXOnScreen();
        int y = e.getYOnScreen();

        // 커서 타입에 따라 크기 조절 (보류... 일단 크기 고정)
        switch (cursorType) {
            case Cursor.NW_RESIZE_CURSOR:
            	setBounds(x-initialClick.x, y-initialClick.y, initialRect.width  - (x- initialRect.x - initialClick.x), initialRect.height - (y- initialRect.y - initialClick.y));
                break;
            case Cursor.NE_RESIZE_CURSOR:
            	setBounds(initialRect.x, y-initialClick.y, initialRect.width + (x - initialRect.x - initialClick.x), initialRect.height - (y- initialRect.y - initialClick.y));
                break;
            case Cursor.SW_RESIZE_CURSOR:
                setBounds(x-initialClick.x, initialRect.y, initialRect.width  - (x- initialRect.x - initialClick.x), initialRect.height + (y- initialRect.y - initialClick.y));
                break;
            case Cursor.SE_RESIZE_CURSOR:
                setBounds(initialRect.x, initialRect.y, initialRect.width + (x - initialRect.x - initialClick.x), initialRect.height + (y- initialRect.y - initialClick.y));
                break;
            case Cursor.W_RESIZE_CURSOR:
            	setBounds(x-initialClick.x, initialRect.y, initialRect.width  - (x- initialRect.x - initialClick.x), initialRect.height);
                break;
            case Cursor.E_RESIZE_CURSOR:
            	setSize(initialRect.width + (x - initialRect.x - initialClick.x), initialRect.height);
                break;
            case Cursor.N_RESIZE_CURSOR:
            	setBounds(initialRect.x, y-initialClick.y, initialRect.width, initialRect.height - (y- initialRect.y - initialClick.y));
                break;
            case Cursor.S_RESIZE_CURSOR:
            	setSize(initialRect.width, initialRect.height + (y- initialRect.y - initialClick.y));
                break;
            default:
            	setLocation(x-initialClick.x, y-initialClick.y);
            	break;
        }

        //initialClick = e.getPoint();  // 새로운 기준점 설정
    }
    
    public static Rectangle showDialog(Window parent) {
    	ScreenRectDialog dialog = new ScreenRectDialog(parent);
    	return dialog.getBounds();
    }
}
