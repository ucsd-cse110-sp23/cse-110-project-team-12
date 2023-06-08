/**
 * @author CSE 110 - Team 12
 */
package processing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Saves file to current directory upon accepting auto login
 * Files contains user's email and password
 *
 */
public class SavefileWriter {

	//file with user email and password for login
	public static final String savedUserInfoFile = "savedInfo.txt";

	/**
	 * Gets login info from file if it exists
	 * 
	 * @return user's login information from file
	 */
	public ArrayList<String> getLoginInfo(){
		File file = new File(savedUserInfoFile);
		boolean autoLogin= file.exists();
		//File exists, means auto login has been set.
		if (autoLogin){
			try{
				ArrayList<String> result = new ArrayList<String>();
				Scanner scanner = new Scanner(file);
				String savedEmail = scanner.nextLine();
				String savedPassword = scanner.nextLine();
				result.add(savedEmail);
				result.add(savedPassword);
				scanner.close();
				return result;
			}
			catch (IOException e){

			}

		}
		return null;
	}

	/**
	 * Method called when user agrees to auto login on device
	 * 
	 * @param user's info to use upon next login
	 */
	public void setLoginInfo(ArrayList<String> info){
		//User selects save my info
		File file = new File(savedUserInfoFile);

		if (info != null){

			try{
				String email = info.get(0);
				String password = info.get(1);
				file.createNewFile();
				FileWriter writer = new FileWriter(file, false);
				writer.write(email + "\n" + password);
				writer.close();
			}
			catch(IOException e){
				//IO exception 
				e.printStackTrace();
			}
		} else {
			file.delete();
		}
	}

}
