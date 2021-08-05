package test.battleTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import test.testHelpers.BetterHelper;
import unsw.loopmania.enemies.StageBossPet;
import unsw.loopmania.status.Status;
import unsw.loopmania.Character;

public class PetBattleTest {
    
    @Test
    public void testAttack(){
        BetterHelper helper = new BetterHelper();
        StageBossPet pet = helper.testStageBossPet1;
        Character hero = helper.testCharacter;
        
        pet.setCritChance(0);
        hero.setCritChance(0);
        
        // Check stats
        assertTrue(pet.getCurrHP() == 100);
        assertTrue(pet.getCurrATK() == 20);
        assertTrue(pet.getCurrDEF() == 2);
        
        assertEquals(hero.getLevel() , 1);
        assertEquals(hero.getMaxHP() , 100);
        assertEquals(hero.getBaseATK() , 2);
        assertEquals(hero.getBaseDEF() , 1);


        pet.attack(hero, null, null);
        assertTrue(hero.getCurrHP() == (100 - 20 + 1));    // Pet has ATK of 20, hero has DEF of 1

        hero.attack(pet, null, null);
        assertTrue(pet.getCurrHP() == (100 - 2 + 2));    // hero has ATK of 20, but pet has DEF of 2
    }

    @Test
    public void testCritAttackStun(){
        BetterHelper helper = new BetterHelper();
        StageBossPet pet = helper.testStageBossPet1;
        Character hero = helper.testCharacter;

        // Critically attack the vampire and apply burn
        pet.critAttack(hero, null, null);
        assertTrue(hero.getStatusIDs().contains("Stunned"));
        assertTrue(hero.getCurrHP() < (100 - (20 * 1.5 - 2)));

        // Character is stunned for one attack
        assert(pet.getCurrHP() == 100);
        // Tick the stun status
        for(Status s: hero.getStatusList()){
            s.battleTick(hero);
        }
        hero.attack(pet, null, null);
        
        for(Status s: hero.getStatusList()){
            s.battleTick(hero);
        }
        // Character is stunned for second attack
        assert(pet.getCurrHP() == 100);
        
        hero.attack(pet, null, null);

        for(Status s: hero.getStatusList()){
            s.battleTick(hero);
        }
        // Character can now attack
        assert(pet.getCurrHP() == 99);
    }

    @Test
    public void testCritAttackBurn(){
        BetterHelper helper = new BetterHelper();
        StageBossPet pet = helper.testStageBossPet2;
        Character hero = helper.testCharacter;

        // Critically attack the vampire and apply burn
        pet.critAttack(hero, null, null);
        assertTrue(hero.getStatusIDs().contains("Burned"));
        assertTrue(hero.getCurrHP() < (100 - (20 * 1.5 - 2)));

        // heal hero back up
        hero.setCurrHP(100);

        pet.attack(hero, null, null);
        assertTrue(hero.getStatusIDs().contains("Burned"));
        assertTrue(hero.getCurrHP() < (100 - (20 - 2))); // should be dealt more damage
    }

}
