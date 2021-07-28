package thread;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//import javax.swing.JFrame;

public class PacMan extends Frame implements Runnable {
	private Image img, foodImg;
	private int sel = 2; // 이미지 sel
	private int x = 225, y = 225; // 이미지 좌표
	private int mx, my; // 전진값
	private int[] foodX, foodY; // 음식 좌표 
	
	// 팩맨 음식 : 5개를 뿌린다. 
	int[] numX = new int[5];
	int[] numY = new int[5];
	public PacMan() {
		img = Toolkit.getDefaultToolkit().getImage("Packman.png");
		foodImg = Toolkit.getDefaultToolkit().getImage("Coin1.png");
		
		// 음식 이미지 좌표값 : 랜덤값으로 뿌린다. 
		foodX = new int[5];
		foodY = new int[5];
		for(int i = 0; i < foodX.length; i++) {
			foodX[i] = (int)(Math.random()*461)+20; // frame에 걸리지 않도록 주의 
			foodY[i] = (int)(Math.random()*461)+20; 
		}
		setBounds(700, 200, 500, 500);
		setVisible(true);
		setResizable(false);
		setTitle("팩맨");

		// 스레드
		Thread t = new Thread(this);
		t.start();

		// 키보드 이벤트
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					sel = 4;
					mx = 0;
					my -= 10;
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					sel = 6;
					mx = 0;
					my += 10;
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					sel = 2;
					mx = 10;
					my = 0;
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					sel = 0;
					mx -= 10;
					my = 0;
				}
				repaint();
			}
		});
		
		// 창닫기
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	} // pacMan constructor

	// 팩맨 그리기
	@Override
	public void paint(Graphics g) {
		// 팩맨 먹이 
		for(int i = 0; i < foodX.length; i++) {
			g.drawImage(foodImg, foodX[i], foodY[i], this);
		}
		
		// 팩맨
		g.drawImage(img, x, y, x+50, y+50, // 화면위치
					sel*50, 0, sel*50+50, 50, // 이미지 위치 
					this);
	}

	// 스레드
	@Override
	public void run() {
		while (true) {
			if (sel % 2 == 0) {
				sel++;
			} else {
				sel--;
			}
			
			x += mx;
			y += my;
			// 스스로 움직이게 
			
			if(x > 460) { // 오른쪽 끝으로 도달 
				sel = 0; // 왼쪽으로 돈다 
				mx -= 10;
				my = 0;
			}else if(x < 15) { // 왼쪽 끝으로 도달 
				sel = 2; // 오른쪽으로 돈다 
				mx += 10;
				my = 0;
			}else if(y > 460) { // 위쪽 끝으로 도달
				sel = 4; // 아래로 돈다 
				mx = 0;
				my -= 10;
				
			}else if(y < 40) { // 아래쪽 끝으로 도달
				sel = 6;// 위로 돈다.
				mx = 0;
				my += 10;
			}
			
			repaint();
			// 입을 움직인다

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// 먹이 먹는 동작
			for(int i = 0; i < foodX.length; i++) {
				if(x+25 >= foodX[i]-5 &&  y+24 >= foodY[i]-5 && x+25 <= foodX[i] + 20 && y+25 <= foodY[i] + 20) {
					foodX[i] = foodY[i] = -10; // 먹은 음식은 프레임 밖으로 보낸다. 
				}
			}

		} // while

	}

	public static void main(String[] args) {
		PacMan pm = new PacMan();

	}

}