/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

/**
 *
 * @author Usuario
 */
public enum State {

    LOGIN(0), SELECTING_OPPONENT(1);

    public int state;

    State(int state) {
        this.state = state;
    }
}
