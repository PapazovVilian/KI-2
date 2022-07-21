package agenten;

public class Pair implements Comparable {
    public Integer index;
    public Integer value;

    @Override
    public int compareTo(Object o) {
        if (((Pair) o).value < this.value) {
            return 1;
        } else if (((Pair) o).value > this.value){
            return -1;
        }else{
            return 0;
        }
    }
}
