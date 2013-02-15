package net.ember.input;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;


/**
 * A gamepad using Microsoft's XInput library via JNI/a DLL.
 * TODO standardise this with other controllers.
 * @author Charlie
 *
 */
public class XInputXboxController {

	private int index;
	private ByteBuffer bb;
	private ShortBuffer sb;
	
	int battery;
	
	public boolean connected = false;
	public boolean voiceSupported = false;
	public boolean wireless = false;
	
	int buttons=0, prevbuttons=0;
	
	/**
	 * Left and right trigger travel, from 0f to 1f
	 */
	public float lt=0.0f,rt=0.0f;
	public float lx=0f,ly=0f,rx=0f,ry=0f;
	
	/**
	 * Obtain the latest information from the controller.
	 */
	public void poll(){
		
		XInput.poll(index, bb);
		if(sb.get()!=1){
			System.err.println("Poll of controller " + index + " failed!");
		}
		
		buttons = sb.get();
		//TODO read axes
		lt=sb.get()*XInput.recipMaxTriggerTravel;
		rt=sb.get()*XInput.recipMaxTriggerTravel;
		lx=sb.get()*(1f/32767f);
		ly=sb.get()*(1f/32767f);
		rx=sb.get()*(1f/32767f);
		ry=sb.get()*(1f/32767f);

		//if((lx*lx+ly*ly)<XInput.XINPUT_GAMEPAD_LEFT_THUMB_DEADZONE_SQUARED){
			//Deadzone says no.
		//}

		bb.clear();
		sb.rewind();
		
	}

	/**
	 * Create an object to wrap around an Xbox controller.
	 */
	public void init() {
		
		bb = ByteBuffer.allocateDirect(16);//Maximum needed is 8 shorts = 16 bytes.
		bb.order(ByteOrder.nativeOrder());
		sb = bb.asShortBuffer();
		
		/**
		 * Find a suitable controller ID to use, if any.
		 */
		index=-1;
		while(index<3&&!connected){//While the index loves you, and isn't otherwise connected......?
			index++;
			XInput.getCapabilities(index, bb);
			if(sb.get()==1){
				connected=true;
			}
			else{
				bb.clear();
				sb.rewind();
			}
		}
		
		if(!connected){
			System.out.println("No XInput controllers found connected to the system.");
			return;
		}
		
		//Log.debug("Using controller index "+index);
		
		/**
		 * Read the controller's capabilities and type.
		 */
		
		int flag = sb.get();
		if((flag&XInput.XINPUT_CAPS_VOICE_SUPPORTED)!=0){
			voiceSupported = true;
		}
		if(sb.get()!=0)System.out.println("Unknown type in capabilities - not a gamepad?");
		
		int subtype = sb.get();
		if(subtype!=XInput.XINPUT_DEVSUBTYPE_GAMEPAD) System.out.println("Not a standard gamepad, but some other device - drums, wheel?");
		
		bb.clear();
		sb.rewind();
		XInput.getBatteryState(index, bb);
		
		if(sb.get()!=1) System.err.println("Cannot read battery state.");
		battery = sb.get();
		int batterytype = sb.get();
		
		if((battery&XInput.BATTERY_TYPE_WIRED)!=0)
			wireless=false;
		else 
			wireless=true;
		
		if(wireless){
			System.out.println("Wireless controller "+index+" has level "+XInput.getBatteryLevel(battery)+" and is a "+XInput.getBatteryType(batterytype)+" battery.");
		}else{
			System.out.println("Wired controller "+index+" connected.");
		}
		
		bb.clear();
		sb.rewind();
		
		poll();
		
	}

	public void checkStatus() {
		//Log.debug("Checking status.");
		if(wireless&&connected){
			bb.clear();
			sb.rewind();
			XInput.getBatteryState(index, bb);
			
			
			if(sb.get()!=1) System.err.println("Cannot read battery state.");
			int batterylevel = sb.get();
			if(batterylevel!=battery){
				System.out.println("Battery level changed.");
				battery=batterylevel;
			}
			int batterytype = sb.get();
			
			bb.clear();
			sb.rewind();
			
		}
		if(!connected){
			//TODO check for connection
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean[] getButtons() {
		return new boolean[]{ 
				((buttons&XInput.BUTTON_A)!=0),
				((buttons&XInput.BUTTON_B)!=0),
				((buttons&XInput.BUTTON_X)!=0),
				((buttons&XInput.BUTTON_Y)!=0),
				((buttons&XInput.BUTTON_LEFT_SHOULDER)!=0),
				((buttons&XInput.BUTTON_RIGHT_SHOULDER)!=0),
				((buttons&XInput.BUTTON_LEFT_THUMB)!=0),
				((buttons&XInput.BUTTON_RIGHT_THUMB)!=0),
				((buttons&XInput.BUTTON_START)!=0),
				((buttons&XInput.BUTTON_BACK)!=0)
		};
	}
}
