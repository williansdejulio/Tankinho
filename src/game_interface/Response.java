/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

/**
 *
 * @author u13181
 */
public enum Response {
    UPDATE("update"), GAME_ANSWER("confirmation"), GAME_INVITE("enemyconfirmation"),
    END("end"), GAME_AVAILABLE("gameavailable");

    public String response;

    Response(String response) {
        this.response = response;
    }

}
