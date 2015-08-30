/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.bullet;

/**
 *
 * @author Leonardo
 */
public enum Debuff {
    NOTHING(0),SILENCE(1),SLOW(2),STUN(3),POISON(4);
    
    public int debuff;
    
    Debuff(int debuff){
        this.debuff = debuff;
    }
}
