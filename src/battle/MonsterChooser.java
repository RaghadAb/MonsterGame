package battle;

import monster.Monster;

import java.util.Set;

public interface MonsterChooser {
	
	public Monster chooseAttackMonster(Set<Monster> monsters);
	
	public Monster chooseDefenseMonster(Set<Monster> monsters);
}
