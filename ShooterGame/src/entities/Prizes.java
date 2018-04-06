package entities;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Prizes extends Movable {

        private final ImageView imageView;
        private final int count;
        private final int columns;
        private int offsetX;
        private int offsetY;
        private final int width;
        private final int height;

        public Prizes(ImageView imageview, Duration duration, int count, int columns, int offsetX, int offsetY, int width, int height){
            this.imageView = imageview;
            this.count = count;
            this.columns = columns;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.width = width;
            this.height = height;

            setCycleDuration(duration);
            setCycleCount(Animation.INDEFINITE);
            setInterpolator(Interpolator.LINEAR);
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        }

        public void setOffsetX(int x){
            this.offsetX = x;
        }

        public void setOffsetY(int y){
            this.offsetY = y;
        }

        protected void interpolate (double frac){
            final int index = Math.min((int)Math.floor(count*frac), count-1);
            final int x = (index%columns)*width+offsetX;
            final int y = (index/columns)*height+offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
        }

    }

