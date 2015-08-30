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
public enum Request {
    UPDATE("update"), GAME_INVITE("enemyconfirmation"), GAME_ANSWER("confirmation"),
    EXIT("exit"), LOGIN("login"), UPDATE_GAME_ENDED("updategameended"), SIGNUP ("signup"),
    END("end"), GAME_AVAILABLE("gameavailable");

    public String request;

    Request(String request) {
        this.request = request;
    }
}
