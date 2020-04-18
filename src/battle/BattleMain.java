package battle;

import java.util.Scanner;

public class BattleMain {
	public static void main(String[] args) throws Exception {
		Trainer t1 = Trainer.loadFromFile("trainer1.txt");
		t1.setMonsterChooser(new HumanMonsterChooser(new Scanner(System.in)));
		Trainer t2 = Trainer.loadFromFile("trainer2.txt");
		t2.setMonsterChooser(new ComputerMonsterChooser());
		
		Battle b = new Battle (t1, t2);
		Trainer winner = b.doBattle();
		System.out.println("Winner is: " + winner.getName());
	}
}
