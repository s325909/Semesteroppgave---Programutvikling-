package entities;

import gameCode.DataHandler;
import gameCode.Game;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class which handles the most basic statistics of every Entity in the gameWindow.
 * This includes positioning in the window, creating a Node for collision detection,
 * creating an AnimationHandler object which contains the images for the Entity.
 */
public class Entity{

    private int positionX;
    private int positionY;

    private boolean alive;
    private boolean drawn;

    private Node node;
    private AnimationHandler animationhandler;

    /**
     * Constructor which creates an AnimationHandler to handle the Entity's images, and creates a Node
     * on the same position as the object, which is then used for collision check.
     * @param allAnimation Requires an AnimationHandler object, see AnimationHandler.
     * @param positionX Requires a desired X-coordinate of where to place object.
     * @param positionY Requires a desired Y-coordinate of where to place object.
     */
    public Entity(AnimationHandler allAnimation, int positionX, int positionY) {
        this.animationhandler = allAnimation;
        this.node = new Circle(this.animationhandler.getWidth() / 2, this.animationhandler.getHeight() / 2, 2 * this.animationhandler.getHeight() / 5, Color.BLUE);
        this.positionX = positionX;
        this.positionY = positionY;
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
        this.alive = true;
    }

    /**
     * Method which draws the ImageView and Node of the Entity to the gameWindow Pane.
     * Will draw the ImageView containing the Image to the gameWindow Pane, including the Node if DEBUG is set to true.
     * Sets the boolean drawn to true, as this method is continuously called in the onUpdate() method in Game, to duplicate Exception.
     * @param game Requires the Game object of which to draw the Image and Node to.
     */
    public void drawImage(Game game) {
        if(!drawn) {
            if(game.isDEBUG())
                game.getGameWindow().getChildren().add(getNode());
            game.getGameWindow().getChildren().add(getAnimationHandler().getImageView());
            drawn = true;
        }
    }

    /**
     * Method which removes the ImageView and Node of the Entity from the gameWindow Pane.
     * Will remove the ImageView containing the Image from the gameWindow Pane, including the Node if DEBUG is set to true.
     * This method is run continuously in the onUpdate() method in Game, and as such only removes the Entity if set to dead.
     * @param game Requires the Game object of which to remove the Image and Node from.
     */
    public void removeImage(Game game) {
        if(!alive){
            if(game.isDEBUG())
                game.getGameWindow().getChildren().remove(getNode());
            game.getGameWindow().getChildren().remove(getAnimationHandler().getImageView());
        }
    }

    /**
     * Method which handles animation cycling via setFrame() call.
     * @param time Requires the Game's timer.
     */
    public void update(double time) {
        getAnimationHandler().setFrame(time);
    }

    /**
     * Method for checking for collision between two Entity object's by using their given Nodes.
     * @param otherEntity Requires an Entity object.
     * @return Returns a boolean based on whether there is collision.
     */
    public boolean isColliding(Entity otherEntity) {
        return this.node.getBoundsInParent().intersects(otherEntity.getNode().getBoundsInParent());
    }

    /**
     * Method which will retrieve and return requested information about an Entity object.
     * Creates a new EntityConfiguration object from the DataHandler class, and transfers
     * variables specific to the Entity class into the corresponding variables in entityCfg.
     * @return Returns the object entityCfg of type EntityConfiguration.
     */
    public DataHandler.EntityConfiguration getEntityConfiguration() {
        DataHandler.EntityConfiguration entityCfg = new DataHandler.EntityConfiguration();
        entityCfg.posX = getPositionX();
        entityCfg.posY = getPositionY();
        return entityCfg;
    }

    /**
     * Method which will transfer provided entityCfg's variables into corresponding variables in Entity.
     * @param entityCfg Requires an object of type EntityConfiguration.
     */
    public void setEntityConfiguration(DataHandler.EntityConfiguration entityCfg) {
        setPosition(entityCfg.posX, entityCfg.posY);
        setTranslateNode(entityCfg.posX, entityCfg.posY);
    }

    /**
     * Method which places the Node in the same position as the Entity object.
     * @param positionX Requires Entity's position in X-coordinate.
     * @param positionY Requires Entity's position in Y-coordinate.
     */
    void setTranslateNode(int positionX, int positionY) {
        this.node.setTranslateX(positionX);
        this.node.setTranslateY(positionY);
    }

    void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isDead() {
        return !alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) { this.positionX = positionX; }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) { this.positionY = positionY; }

    public AnimationHandler getAnimationHandler() {
        return animationhandler;
    }

    public Node getNode() {
        return node;
    }

    public boolean isDrawn() { return drawn; }

    public void setDrawn() { drawn = true; }
}