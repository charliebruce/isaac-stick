import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import net.ember.input.XInputXboxController;


public class IsaacStick {

	public static void main(String[] args)
	{
		try {
			
			//Load the XInput DLL nexessary for interacting with the xbox controller.
			NativeLoader.loadNativeLibraries();
			
			Robot r = new Robot();

		    //Runtime.getRuntime().exec("C:\\Program Files\\Steam\\steam.exe -applaunch 113200");
		
			//Create the controller object and initialise it
		    XInputXboxController ctrl = new XInputXboxController();
		    ctrl.init();
		    
		    boolean exit = false;
		    boolean[] buttons;
		    
		    while(!exit){
		    	ctrl.poll();
		    	
		    	buttons = ctrl.getButtons();
		    	
		    	float rx = ctrl.rx;
		    	float ry = ctrl.ry;
		    	
		    	
		    	
		    	
		    	float lx = ctrl.lx;
		    	float ly = ctrl.ly;
		    	
		    	if ((lx*lx+ly*ly)>(0.4*0.4)) {
		    		if (lx > 0.28){
		    			r.keyPress(KeyEvent.VK_D);
		    		} else {
		    			r.keyRelease(KeyEvent.VK_D);
		    		}
		    		
		    		if (lx < -0.28){
		    			r.keyPress(KeyEvent.VK_A);
		    		} else {
		    			r.keyRelease(KeyEvent.VK_A);
		    		}
		    		
		    		if (ly > 0.28){
		    			r.keyPress(KeyEvent.VK_W);
		    		} else {
		    			r.keyRelease(KeyEvent.VK_W);
		    		}
		    		
		    		if (ly < -0.28){
		    			r.keyPress(KeyEvent.VK_S);
		    		} else {
		    			r.keyRelease(KeyEvent.VK_S);
		    		}
		    		
		    		
		    	} else {
		    		r.keyRelease(KeyEvent.VK_W);
		    		r.keyRelease(KeyEvent.VK_A);
		    		r.keyRelease(KeyEvent.VK_S);
		    		r.keyRelease(KeyEvent.VK_D);
		    	}
		    	
		    	//A,B,X,Y,LS,RS,LT,RT,Start,Back
		    	if (buttons[9]) {
		    		exit = true;
		    	}
		    	
		    	if(buttons[0]) {
		    		r.keyPress(KeyEvent.VK_SPACE);
		    	} else {
		    		r.keyRelease(KeyEvent.VK_SPACE);
		    	}
		    	
		    	Thread.sleep(50);
		    }
		    
					
		    
		
		}catch(AWTException ex){
			System.err.println("AWT Exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		
	}
}
