
import java.io.File;


public class NativeLoader {

	/**
	 * Load all of the native code required.
	 * @throws Exception 
	 */
	public static void loadNativeLibraries() throws Exception {
		//Find OS and choose the native folders appropriately.
		String osname = System.getProperty("os.name");
		String osarch = System.getProperty("os.arch");

		System.out.println("Platform: Running "+osname+" "+osarch+", version "+System.getProperty("os.version")+".");
		String os = "osx";
		String ext = ".dll";
		if (osname.toLowerCase().contains("linux")||osname.toLowerCase().contains("unix")) {os="linux";ext=".so";}
		if (osname.toLowerCase().contains("windows")) {os="win"; ext=".dll";}
		String arch = "x86";
		if(osarch.contains("64")) arch="x64";


		//XInput
		if(os.equals("win")){
			File f = new File("lib/win-"+arch+"/xinput.dll");
			if(f.exists()){
				System.load(f.getAbsolutePath());
				System.out.println("Loaded xinput DLL");
			}
			else{
				System.err.println("Unable to load XInput - DLL not found");
				throw new Exception();
			}
		}else{
			//We cannot use XInput library on non-Windows platforms. No rumble for Mac/Linux :(
			System.err.println("Unable to load XInput - not Windows");
			throw new Exception();
		}
		
	}

}
