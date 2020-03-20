package SibsInterface;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayInterface {

	public static void main(String[] args) throws SibsException, AccountException, OperationException {
		
		Scanner s = new Scanner(System.in);
		
		MBWayInterfaceModel mbmodel = new MBWayInterfaceModel();	
		MBWayInterfaceView mbview = new MBWayInterfaceView();
		
		MBWayInterfaceController mbcontroller = new MBWayInterfaceController(mbmodel, mbview);
	
		String input;
		
		while (mbcontroller.isRunning()){
			
			input = s.nextLine();

			mbcontroller.setUserInput(input); 
			
            mbcontroller.updateView(); 
                
		}
		
		s.close();

	}

}
