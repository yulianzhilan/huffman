package sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by scott on 2017/4/12.
 */
public class SortUtil{

    static class Data implements Comparable<Data>{
        private int order;

        private String info;

        public Data() {
        }

        public Data(int order) {
            this.order = order;
        }

        public Data(int order, String info) {
            this.order = order;
            this.info = info;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public int compareTo(Data o) {
            return this.order - o.order;
        }

        @Override
        public String toString() {
            return order+"";
        }
    }

    public static long selectSort(Data[] data){
        if(data == null || data.length == 0){
            return 0;
        }
        long step = 0;
        for(int i = 0; i < data.length; i++){
            for(int j =i; j < data.length; j++,step++){
                if(data[i].compareTo(data[j])>0){
                    Data temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        return step;
    }

    public static long selectSortClearify(Data[] data){
        if(data == null || data.length == 0){
            return 0;
        }
        long step = 0;
        for(int i = 0; i < data.length-1; i++){
            int min = i;
            for(int j = i+1; j < data.length; j++,step++){
                if(data[i].compareTo(data[j])>0){
                    min = j;
                }
            }
            Data temp = data[i];
            data[i] = data[min];
            data[min] = temp;
        }
        return step;
    }

//    public static long bubbleSort(Data[] data){
//
//        for(int i = 0; i < data.length; i++){
//
//            for(int j = i; j < data.length; j++){
//
//            }
//
//        }
//
//
//    }

    public static void main(String[] args) {
        Data[] data = {
                new Data(12),
                new Data(56),
                new Data(2),
                new Data(54),
                new Data(4),
                new Data(789),
                new Data(89),
                new Data(89),
                new Data(9),
                new Data(12),
                new Data(56),
                new Data(2),
                new Data(54),
                new Data(4),
                new Data(789),
                new Data(89),
                new Data(89),
                new Data(9),
                new Data(79)
        };
//        System.out.println(Arrays.toString(data));
//        System.out.println(selectSort(data));
        System.out.println(Arrays.toString(data));
        System.out.println(selectSortClearify(data));
        System.out.println(Arrays.toString(data));

    }
}
