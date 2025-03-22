package adt;

/**
 *
 * @author Raymond
 */
public class OrderPair<X,Y> {
    private X first;
    private Y second;
    
    public OrderPair(X item1, Y item2){
        this.first = item1;
        this.second = item2;
    }
    
    public X getX(){
        return first;
    }
    
    public void setX(X item){
        this.first = item;
    }

    public Y getY(){
        return second;
    }

    public void setY(Y item){
        this.second = item;
    }
}
