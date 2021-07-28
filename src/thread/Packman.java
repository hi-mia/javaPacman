package thread;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Packman extends JFrame implements KeyListener, Runnable {
   private int x = 225, y=225;
   int sel = 2;
   private Image img, foodImg;
   private int mx, my; //전진값
   private int[] foodX, foodY; //음식 좌표 
   
   //팩맨 음식: 5개를 뿌린다
   int[] numX = new int[5];
   int[] numY = new int[5];
   
   public Packman() {
	      img =  Toolkit.getDefaultToolkit().getImage("Packman.png");
	      //이미지 불러온거지 이미지 그린거 아님 -> paint 메소드
	      foodImg = Toolkit.getDefaultToolkit().getImage("Coin1.png");
	      
	      //먹이 좌표 : 랜덤
	      foodX = new int[5];
	      foodY = new int[5];
	      for(int i = 0; i<foodX.length;i++) {
	    	  foodX[i] = (int)(Math.random()*451)+20;
	    	  foodY[i] = (int)(Math.random()*451)+20;
	      }
      
      setBackground(Color.WHITE);
      
      setTitle("팩맨");
      setBounds(700,200,500,500); //창 크기
      setResizable(false);//창크키 고정
      setVisible(true);//창 보여주기
      setDefaultCloseOperation(EXIT_ON_CLOSE);//시스템끄기

      //이벤트
      this.addKeyListener(this);

      Thread t = new Thread(this); //스레드 생성
      t.start(); //스레드 시작 - 스레드 실행(run())
      
   }//Packman()
   
   //키보드
   @Override
   public void keyPressed(KeyEvent e) {
      if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
         System.exit(0);
      }else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
         mx -= 10; my = 0;
         sel = 0;
         if(x<-30) x=480;
      }else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
    	  mx += 10; my = 0; 
    	  sel = 2;
         if(x>480) x=-30;
      }else if(e.getKeyCode()==KeyEvent.VK_UP) {
         my -= 10; mx = 0;
         sel = 4;
         if(y<0) y=470;
      }else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
         my += 10; mx = 0;
         sel = 6;
         if(y>470) y=0;
      }
         repaint();
   }
   
  //팩맨
   @Override
   public void paint(Graphics g) {
	   super.paint(g);
      
	   //이미지 위치 - 팩맨
	   g.drawImage(img,
	             x,y,x+50,y+50, //화면 위치 225, 225, 275, 275
	             sel*50,0,sel*50+50,50,  //이미지 위치
	             this); //나에게 그려라
	   
      //팩맨 먹이
	      for(int i = 0; i<foodX.length; i++) {
	    	  g.drawImage(foodImg, foodX[i], foodY[i], this);
	      }
	 // g.drawImage(foodImg, foodX[0], foodY[0], 20, 20, this); //이미지 클 때 줄임
	      g.drawImage(foodImg, foodX[0], foodY[0], this);

   }
   
   @Override
   public void run() { //계속 그려야 -> repaint를 run메소드가 가져야
	   while(true) {
		  //좌표
		   if(sel%2 == 0) sel++;
		   else sel--;
		   
		   x += mx;
		   y += my;
		   
		   if(x>500) x = 0;
		   else if(x < 0) x=500;
		   
		   if(y>500) y = 0;
		   else if(y < 0) y=500;
		   
		   repaint(); //입을 움직인다
		   
		  try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		  
		  // 먹이를 먹자
		  for(int i = 0; i< foodX.length; i++) { //5개 먹이 먹어라
			  if(x+25 >= foodX[i]-5 &&  y+25 >= foodY[i]-5 
					  && x+25 <= foodX[i] + 20 && y+25 <= foodY[i] + 20) {
					foodX[i] = foodY[i] = -100;
			  }
		 }
	   } //while
   } //run() 
   
   //KeyListener Override
   @Override
   public void keyTyped(KeyEvent e) {
   }
   
   @Override
   public void keyReleased(KeyEvent e) {
      
   }
 //---------------------------------------------------  
   public static void main(String[] args) {
      new Packman();

   }
}


/*
 * 1. 먹이 이미지 20,20 / *도 가능
 * 2. 먹이 5마리 - foodX, foodY(먹이 위치) / 랜덤하게 깔아주기 / 먹이 경계선 안에
 * 3. 먹이 먹으면 다시 그림 - 먹이 이미지 -100,-100으로(좌표를 안 보이는 곳에다가 찍음) / 같은 색으로 다시 덮음
 * 4. if문으로 좌표 물어보기 / 먹이-팩맨 같은 위치? 물어봄 // 먹이는 입으로 먹어야
 * 입 좌표: x+25, y-25
 * if(x+25 == 300) / 너무 정확하게 하면X
 * 먹이는 20x20이지만 실제로는 좀 더 크게(+10) 잡아줘야 함. 이 영역 안에 들어오면 먹은 거라고 인정
 * -> 먹이 사라짐
 * if문만 잘 걸어주면 됨
 */
