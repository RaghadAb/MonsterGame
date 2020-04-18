package battle;

import monster.Monster;
import monster.Type;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Represents a Trainer, who has a set of Monsters and can battle other
 * Trainers.
 */
public class Trainer {

	/** This Trainer's name */
	private String name;
	/** The set of Monsters on this Trainer's team */
	private Set<Monster> monsters;
	/** The mechanism for choosing monsters */
	private MonsterChooser monsterChooser;

	/**
	 * Creates a new trainer with the given name.
	 * 
	 * @param name The name of the Trainer
	 */
	public Trainer(String name) {
		this.name = name;
		this.monsters = new HashSet<>();
	}
	
	/**
	 * Sets the monster chooser
	 */
	public void setMonsterChooser(MonsterChooser monsterChooser) {
		this.monsterChooser = monsterChooser;
	}

	/**
	 * Adds a Monster to this Trainer's set.
	 * 
	 * @param m The Monster to add
	 */
	public void addMonster(Monster m) {
		this.monsters.add(m);
	}

	/**
	 * Returns all Monsters owned by this Trainer.
	 * 
	 * @return The set of Monster
	 */
	public Set<Monster> getMonsters() {
		return monsters;
	}

	/**
	 * Returns this Trainer's name
	 * 
	 * @return This Trainer's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the set of Monsters owned by this Trainer, divided by their type.
	 * 
	 * @return All of this Trainer's Monsters, categorised by type
	 */
	public Map<Type, Set<Monster>> getMonstersByType() {
		Map<Type, Set<Monster>> result = new HashMap<>();

		for (Monster m : monsters) {
			Set<Monster> typeMonsters = result.get(m.getType());
			if (typeMonsters == null) {
				typeMonsters = new HashSet<>();
				result.put(m.getType(), typeMonsters);
			}
			typeMonsters.add(m);
		}
		return result;
	}

	/**
	 * Checks whether this Trainer can still continue in a battle.
	 * 
	 * @return True if there is at least one monster with positive HP, and false if
	 *         not
	 */
	public boolean canFight() {
		return monsters.stream().filter(m -> m.getHitPoints() > 0).findAny().isPresent();
	}

	/**
	 * Loads a Trainer from the given file and returns the result.
	 * 
	 * @param filename The file to load from
	 * @return A Trainer based on the information in the file
	 * @throws IOException               If the file cannot be accessed
	 */
	public static Trainer loadFromFile(String filename)
			throws IOException {
		// Load the whole file into a List<String> in memory
		Path p = Paths.get(filename);
		List<String> lines = Files.readAllLines(p);

		// First line is name
		String name = lines.remove(0);
		Trainer trainer = new Trainer(name);

		// Process the rest of the lines into Monster objects
		for (String line : lines) {
			// Split the line
			String[] fields = line.split("\\^");
			// Use the fields to create a new Monster and add it
			Monster monster = new Monster(fields[0], Type.valueOf(fields[1]), Integer.parseInt(fields[2]),
					Integer.parseInt(fields[3]));
			trainer.addMonster(monster);
		}

		// Return the newly created Trainer
		return trainer;
	}

	/**
	 * Saves the current Trainer to the given file.
	 * 
	 * @param filename The file to save to
	 * @throws IOException If there is an error accessing the file.
	 */
	public void saveToFile(String filename) throws IOException {
		// Open the file
		PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filename)));

		writer.println(this.name);

		// Write the monsters one line at a time
		for (Monster monster : monsters) {
			String[] properties = new String[4];
			int i = 0;
			properties[i++] = monster.getName();
			properties[i++] = monster.getType().name();
			properties[i++] = String.valueOf(monster.getHitPoints());
			properties[i++] = String.valueOf(monster.getAttackPoints());
			writer.println(String.join("^", properties));
		}

		// We are done!
		writer.close();
	}

	public Monster chooseAttackMonster() {
		return monsterChooser.chooseAttackMonster(monsters);
	}
	
	public Monster chooseDefenseMonster() {
		return monsterChooser.chooseDefenseMonster(monsters);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((monsters == null) ? 0 : monsters.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trainer other = (Trainer) obj;
		if (monsters == null) {
			if (other.monsters != null)
				return false;
		} else if (!monsters.equals(other.monsters))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
