package test;

import battle.ComputerMonsterChooser;
import battle.HumanMonsterChooser;
import battle.MonsterChooser;
import battle.Trainer;
import monster.Monster;
import monster.Type;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class Lab7Tester {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Monster pikachu, rowlet, litten, turtonator, alolanMarowak, gyarados, megaGyarados;
	private Trainer ash, kiawe;
	private MonsterChooser cmc;

	@BeforeEach
	void setUp() throws Exception {
		ash = new Trainer("Ash");
		pikachu = new Monster("Pikachu", Type.ELECTRIC, 35, 55);
		rowlet = new Monster("Rowlet", Type.GRASS, 68, 55);
		litten = new Monster("Litten", Type.FIRE, 45, 65);
		ash.addMonster(pikachu);
		ash.addMonster(rowlet);
		ash.addMonster(litten);
		
		turtonator = new Monster("Turtonator", Type.FIRE, 60, 78);
		alolanMarowak = new Monster("Alolan Marowak", Type.FIRE, 60, 80);
		kiawe = new Trainer("Kiawe");
		kiawe.addMonster(turtonator);
		kiawe.addMonster(alolanMarowak);
		
		gyarados = new Monster("Gyarados", Type.WATER, 95, 155);
		megaGyarados = new Monster("Mega Gyarados", Type.WATER, 115, 175);
		
		cmc = new ComputerMonsterChooser();
	}

	@AfterEach
	void tearDown() throws Exception {
		ash = null;
		kiawe = null;
		pikachu = null;
		rowlet = null;
		litten = null;
		turtonator = null;
		alolanMarowak = null;
		gyarados = null;
		megaGyarados = null;
		cmc = null;
	}

	@Test
	public void fireMonsterDodgeWorksProperly() {
		Assert.assertTrue("First call to Fire Monster.dodge() should return true",
				litten.dodge());
		Assert.assertFalse("Second call to Fire Monster.dodge() should return false",
				litten.dodge());
		Assert.assertTrue("Third call to Fire Monster.dodge() should return true",
				litten.dodge());
		Assert.assertTrue("One fire monster dodging should not affect another", turtonator.dodge());
	}
	
	@Test
	public void waterMonsterDodgeWorksProperly() {
		Assert.assertTrue("Water Monster.dodge() should return true if HP is over 100",
				megaGyarados.dodge());
		Assert.assertFalse("Water Monster.dodge() should return false if HP is less than 100",
				gyarados.dodge());
	}
	
	@Test
	public void electricMonsterDodgeWorksProperly() {
		Assert.assertFalse("Electric Monster.dodge() should always return false",
				pikachu.dodge());
		Assert.assertFalse("Electric Monster.dodge() should always return false",
				pikachu.dodge());
	}
	
	@Test
	public void monsterEqualsWorksProperly() {
		Monster pikachu2 = new Monster("Pikachu", Type.ELECTRIC, pikachu.getHitPoints(), pikachu.getAttackPoints());
		Assert.assertEquals("Two identical monsters should be considered equal", pikachu, pikachu2);
		Monster similar = new Monster("Pikachu2", Type.ELECTRIC, pikachu.getHitPoints(), pikachu.getAttackPoints());
		Assert.assertNotEquals("Two similar monsters should not be considered equal", pikachu, similar);
		similar = new Monster("Pikachu", Type.FIRE, pikachu.getHitPoints(), pikachu.getAttackPoints());
		Assert.assertNotEquals("Two similar monsters should not be considered equal", pikachu, similar);
		similar = new Monster("Pikachu", Type.ELECTRIC, pikachu.getHitPoints() + 10, pikachu.getAttackPoints());
		Assert.assertNotEquals("Two similar monsters should not be considered equal", pikachu, similar);
		similar = new Monster("Pikachu", Type.ELECTRIC, pikachu.getHitPoints(), pikachu.getAttackPoints() + 10);
		Assert.assertNotEquals("Two similar monsters should not be considered equal", pikachu, similar);
	}
	
	@Test
	public void grassMonsterDodgeWorksProperly() {
		Assert.assertFalse("ElectricMonster.dodge() should always return false",
				rowlet.dodge());
		Assert.assertFalse("ElectricMonster.dodge() should always return false",
				rowlet.dodge());
	}
		
	@Test
	public void trainerEqualsWorksProperly() {
		Trainer ash2 = new Trainer("Ash");
		ash2.addMonster(new Monster("Pikachu", Type.ELECTRIC, pikachu.getHitPoints(), pikachu.getAttackPoints()));
		ash2.addMonster(new Monster("Rowlet", Type.GRASS, rowlet.getHitPoints(), rowlet.getAttackPoints()));
		ash2.addMonster(new Monster("Litten", Type.FIRE, litten.getHitPoints(), litten.getAttackPoints()));
		Assert.assertEquals("Two identical trainers should be considered equal", ash, ash2);
		
		Trainer ash3 = new Trainer("Ash");
		Assert.assertNotEquals("Two trainers with same name but different monsters are not equal", ash, ash3);
		
		Trainer other = new Trainer("Other guy");
		other.addMonster(new Monster("Pikachu", Type.ELECTRIC, pikachu.getHitPoints(), pikachu.getAttackPoints()));
		other.addMonster(new Monster("Rowlet", Type.GRASS, rowlet.getHitPoints(), rowlet.getAttackPoints()));
		other.addMonster(new Monster("Litten", Type.FIRE, litten.getHitPoints(), litten.getAttackPoints()));
		Assert.assertNotEquals("Two trainers with same monsters but different name are not equal", ash, other);
	}

	@Test
	public void fireMonsterTypeWeaknessesCorrect() {
		Assert.assertTrue ("Fire monster should be weak against Water", Type.FIRE.isWeakAgainst(Type.WATER));
		Assert.assertTrue ("Fire monster should be weak against Fire", Type.FIRE.isWeakAgainst(Type.FIRE));
		Assert.assertFalse ("Fire monster should not be weak against Electric", Type.FIRE.isWeakAgainst(Type.ELECTRIC));
		Assert.assertFalse ("Fire monster should not be weak against Grass", Type.FIRE.isWeakAgainst(Type.GRASS));
	}
	
	@Test
	public void waterMonsterTypeWeaknessesCorrect() {
		Assert.assertTrue ("Water monster should be weak against Water", Type.WATER.isWeakAgainst(Type.WATER));
		Assert.assertTrue ("Water monster should be weak against Grass", Type.WATER.isWeakAgainst(Type.GRASS));
		Assert.assertFalse ("Water monster should not be weak against Electric", Type.WATER.isWeakAgainst(Type.ELECTRIC));
		Assert.assertFalse ("Water monster should not be weak against Fire", Type.WATER.isWeakAgainst(Type.FIRE));
	}
	
	@Test
	public void electricMonsterTypeWeaknessesCorrect() {
		Assert.assertTrue ("Electric monster should be weak against Electric", Type.ELECTRIC.isWeakAgainst(Type.ELECTRIC));
		Assert.assertTrue ("Electric monster should be weak against Grass", Type.ELECTRIC.isWeakAgainst(Type.GRASS));
		Assert.assertFalse ("Electric monster should not be weak against Fire", Type.ELECTRIC.isWeakAgainst(Type.FIRE));
		Assert.assertFalse ("Electric monster should not be weak against Water", Type.ELECTRIC.isWeakAgainst(Type.WATER));
	}
	
	@Test
	public void grassMonsterTypeWeaknessesCorrect() {
		Assert.assertTrue ("Grass monster should be weak against Fire", Type.GRASS.isWeakAgainst(Type.FIRE));
		Assert.assertTrue ("Grass monster should be weak against Grass", Type.GRASS.isWeakAgainst(Type.GRASS));
		Assert.assertFalse ("Grass monster should not be weak against Electric", Type.GRASS.isWeakAgainst(Type.ELECTRIC));
		Assert.assertFalse ("Grass monster should not be weak against Water", Type.GRASS.isWeakAgainst(Type.WATER));
	}
	
	@Test
	public void typeOrderCorrect() {
		Type[] typeOrder = new Type[] { Type.FIRE, Type.WATER, Type.ELECTRIC, Type.GRASS };
		Assert.assertArrayEquals("Type order incorrect", typeOrder, Type.values());
	}
	
	@Test
	public void canFightReturnsTrueIfMonsterAvailable() {
		Assert.assertTrue("canFight() should return true if trainer has at least one monster with hit points", ash.canFight());
	}
	
	@Test
	public void canFightReturnsFalseIfNoMonstersAvailable() {
		Trainer trainer = new Trainer("Test");
		trainer.addMonster(new Monster("M1", Type.ELECTRIC, 0, 400));
		trainer.addMonster(new Monster("M2", Type.FIRE, 0, 200));
		Assert.assertFalse("canFight() should return false if all trainer's monsters are knocked out", trainer.canFight());
	}
	
	@Test
	public void canFightReturnsFalseIfNoMonsters() {
		Trainer trainer = new Trainer("Test");
		Assert.assertFalse("canFight() should return false if trainer has no monsters", trainer.canFight());
	}
	
	@Test
	public void chooseAttackMonsterWorksProperly() {
		Assert.assertEquals("chooseAttackMonster should choose monster with most AP", litten, cmc.chooseAttackMonster(ash.getMonsters()));
	}
	
	@Test
	public void chooseAttackMonsterWorksProperlyWithKnockOut() {
		ash.addMonster(new Monster("M1", Type.ELECTRIC, 0, 400));
		Assert.assertEquals("chooseAttackMonster should choose monster with most AP, ignoring knocked-out monsters", litten, cmc.chooseAttackMonster(ash.getMonsters()));
	}
	
	@Test
	public void chooseDefenseMonsterWorksProperly() {
		Assert.assertEquals("chooseDefenseMonster should choose monster with most HP", rowlet, cmc.chooseDefenseMonster(ash.getMonsters()));
	}
	
	@Test
	public void testHumanPlayerGetAttackMonsterShouldReadFromStdin() {
		// Simulate stdin and stdout
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Scanner scanner = new Scanner("50 aaa 0");
		
		MonsterChooser hmc = new HumanMonsterChooser(scanner);
		Monster m = hmc.chooseAttackMonster(kiawe.getMonsters());
		
		Assert.assertFalse("HumanMonsterChooser.getAttackMonster() should prompt to stdout", baos.toString().isEmpty());
		Assert.assertTrue("Result of HumanMonsterChooser.getAttackMonster() should be one of the trainer's monsters", kiawe.getMonsters().contains(m));
	}
	
	@Test
	public void testHumanPlayerGetDefenseMonsterShouldReadFromStdin() {
		// Simulate stdin and stdout
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Scanner scanner = new Scanner("50 aaa 0");
		
		MonsterChooser hmc = new HumanMonsterChooser(scanner);
		Monster m = hmc.chooseDefenseMonster(kiawe.getMonsters());
		
		Assert.assertFalse("HumanMonsterChooser.getDefenseMonster() should prompt to stdout", baos.toString().isEmpty());
		Assert.assertTrue("Result of HumanMonsterChooser.getDefenseMonster() should be one of the trainer's monsters", kiawe.getMonsters().contains(m));
	}

	// Get a file name for loading/saving wishlists, and makes sure the file doesn't exist before returning
	private Path getOutputFile() throws IOException {
		tempFolder.create();
		Path path = tempFolder.newFile().toPath();
		Files.deleteIfExists(path);
		return path;
	}

	@Test
	public void testSaveAndLoadShouldWorkTogether() throws Exception {
		Path path = getOutputFile();
		ash.saveToFile(path.toString());
		Trainer trainer2 = Trainer.loadFromFile(path.toString());
		Assert.assertEquals("Trainer should be identical after load/save", ash, trainer2);
	}

	@Test
	public void testSaveShouldProduceNonEmptyFile() throws Exception {
		Path path = getOutputFile();
		kiawe.saveToFile(path.toString());
		Assert.assertTrue("Trainer output file does not exist after save()", Files.exists(path));
		Assert.assertTrue("Trainer output file is empty after save()", Files.size(path) > 0);
	}

}
