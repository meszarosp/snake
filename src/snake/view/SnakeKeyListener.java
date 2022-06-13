package snake.view;

import snake.model.Snake;

import java.awt.event.*;

public class SnakeKeyListener extends KeyAdapter {
    /**
     * Kígyó, amelynek a fordulását állítja.
     */
    private Snake snake;


    /**
     * Konkstruktor, amellyel a kígyó beállítható.
     * @param snake Kígyó, amelyiknek a fordulását állítja.
     */
    public SnakeKeyListener(Snake snake) {
        this.snake = snake;
    }

    /**
     * Billentyű lenyomásakor hívódik meg.
     * Az A vagy a balra nyíl lenyomásakor szól a kígyónak, hogy balra kell fordulnia.
     * A D vagy a jobbra nyíl lenyomásakor szól a kígyónak, hogy jobbra kell fordulnia.
     * @param e A KeyEvent, amelyet feldolgoz.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_A || c == KeyEvent.VK_LEFT){
            snake.setLeftRotate(true);
        }else if(c == KeyEvent.VK_D || c == KeyEvent.VK_RIGHT){
            snake.setRightRotate(true);
        }
    }

    /**
     * Billentyű felengedésekor hívódik meg.
     * Az A vagy a balra nyíl felengedésekor szól a kígyónak, hogy már nem kell balra fordulnia.
     * A D vagy a jobbra nyíl felengedésekor szól a kígyónak, hogy már nem kell jobbra fordulnia.
     * @param e A KeyEvent, amelyet feldolgoz.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_A || c == KeyEvent.VK_LEFT){
            snake.setLeftRotate(false);
        }else if(c == KeyEvent.VK_D || c == KeyEvent.VK_RIGHT){
            snake.setRightRotate(false);
        }
    }


}
