package tree;

import java.util.*;

/**
 * 哈夫曼编码解码
 * Created by scott on 2017/4/11.
 */
public class HuffmanCoding{
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.println("please input the message:(end with #@$)");
        String line = in.nextLine();
        StringBuffer bf = new StringBuffer();
        while(line != null){
            if(line.endsWith("#@$")){
                line = line.substring(0,line.length()-3);
                bf.append(line);
                break;
            }
            bf.append(line);
            line = in.nextLine();
        }
        String message = new String(bf);
        List<HuffmanTree.Node> result = countWeight(message);
        System.out.println(result);
        HuffmanTree.Node tree = HuffmanTree.createTree(result);
        System.out.println(tree);
        Map<Character, String> map = getCode(tree);
        System.out.println(map);
        String encode = transfer(map,message);
        System.out.println(encode);
        String decode = distransfer(map,encode);
        System.out.println(decode);
    }

//    public static String encoding(){
//
//    }

    /**
     * 根据编码集将明文转化成密文
     * @param stand
     * @param message
     * @return
     * @throws Exception
     */
    public static String transfer(Map<Character, String> stand, String message) throws Exception{
        if(message == null || "".equals(message)){
            return null;
        }
        char[] chars = message.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(char c : chars){
            String code = stand.get(c);
            if(code == null){
                throw new Exception("unknown exception!");
            }
            sb.append(code);
        }
        return new String(sb);
    }

    /**
     * 根据编码集将密文转化为明文
     * @param stand
     * @param message
     * @return
     */
    public static String distransfer(Map<Character, String>stand, String message) throws Exception{
        StringBuffer sb = new StringBuffer();
        while(message.length()>0){
            int index = 1;
            String each = message.substring(0,index);
            Character match = matched(stand, each);
            while(match == null){
                if(index == message.length()){
                    System.out.println(sb);
                    System.out.println(index);
                    throw new Exception("unmatched code!");
                }
                each = message.substring(0, ++index);
                match = matched(stand, each);
            }
            sb.append(match);
            message = message.substring(index);
        }
        return new String(sb);
    }

    /**
     *
     * @param stand
     * @param each
     * @return
     */
    private static Character matched(Map<Character, String> stand, String each){
        Set<Character> keyset = stand.keySet();
        for(Character key : keyset){
            if(stand.get(key).equals(each)){
                return key;
            }
        }
        return null;
    }

    /**
     * 计算报文的组成字符以及对应的权数
     * @param message
     * @return 节点集合
     */
    public static List<HuffmanTree.Node> countWeight(String message){
        List<HuffmanTree.Node> list = new ArrayList<>();
        if(message == null){
            return null;
        }
        char[] chars =  message.toCharArray();
        for(int i=0; i<chars.length; i++){
            HuffmanTree.Node node = new HuffmanTree.Node(chars[i],1);
            if(!list.contains(node)){
                list.add(node);
            }else{
                list.get(list.indexOf(node)).weight++;
            }
        }
        return list;
    }

    /**
     * 获取简码
     * @param root
     * @return
     */
    public static Map<Character, String> getCode(HuffmanTree.Node root){
        Map<Character, String> map = new HashMap<>();
        calculateCode(root, map, "", "");
        return map;
    }

    /**
     * 这里使用递归去计算每个叶子节点的简码
     * @param root
     * @param source
     * @param prefix
     * @param suffix
     * @return
     */
    private static void calculateCode(HuffmanTree.Node root, Map<Character, String> source, String prefix, String suffix){
        if(root.leftChild != null || root.rightChild != null){
            calculateCode(root.leftChild, source, suffix + prefix, "0");
            calculateCode(root.rightChild, source, suffix + prefix, "1");
        } else{
            source.put((Character)root.data,prefix+suffix);
        }
    }
}


/**
 * 哈夫曼树
 */
class HuffmanTree<E>{
    /**
     * 定义节点类
     * @param <E>
     */
    @SuppressWarnings(value = "unchecked")
    public static class Node<E>{
        E data;
        double weight;
        Node leftChild;
        Node rightChild;

        public Node(E data, double weight){
            this.data = data;
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "Node[data=" + data + ", weight=" + weight + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof Node)){
                return false;
            }
            Node another = (Node) obj;
            if(another.data == null){
                return false;
            }
            if(!this.data.equals(another.data)){
                return false;
            }
            return true;
        }
    }

    /**
     * 生成哈夫曼树
     * @param nodes
     * @return
     */
    public static Node createTree(List<Node> nodes){
        while(nodes.size() > 1){
            quickSort(nodes);
            //获取权数最小的两个节点
            Node left = nodes.get(nodes.size()-1);
            Node right = nodes.get(nodes.size()-2);
            //生成新街点
            Node parent = new Node(null, left.weight + right.weight);
            //让新节点作为权数最小的两个节点的父节点
            parent.leftChild = left;
            parent.rightChild = right;
            //删除最后两个权数最小的节点
            nodes.remove(nodes.size()-1);
            nodes.remove(nodes.size()-1);
            //将新生成的父节点添加到集合中
            nodes.add(parent);
        }
        //返回集合中唯一的节点，也就是根节点
        return nodes.get(0);
    }

    /**
     * 交换位置
     * @param nodes
     * @param i
     * @param j
     */
    private static void swap(List<Node> nodes, int i, int j){
        Node tmp;
        tmp = nodes.get(i);
        nodes.set(i, nodes.get(j));
        nodes.set(j, tmp);
    }

    /**
     * 实现快速排序
     * @param nodes
     * @param start
     * @param end
     */
    private static void subSort(List<Node> nodes, int start, int end){
        //需要排序
        if(start < end){
            // 以第一个元素作为分界值
            Node base = nodes.get(start);
            // 从左边搜索大于分界值的元素的索引
            int i = start;
            int j = end + 1;
            while(true){
                //找到大于分界值的元素的索引
                while(i < end && nodes.get(++i).weight >= base.weight);
                // 找到小于分界值的元素的索引
                while(j > start && nodes.get(--j).weight <= base.weight);
                if(i < j){
                    swap(nodes, i, j);
                } else{
                    break;
                }
            }
            swap(nodes, start, j);
            subSort(nodes, start, j-1);
            subSort(nodes, j+1, end);
        }
    }

    /**
     * 快速排序
     * @param nodes
     */
    public static void quickSort(List<Node> nodes){
        subSort(nodes, 0, nodes.size()-1);
    }

    /**
     * 广度优先遍历
     * @param root
     * @return
     */
    public static List<Node> breadthFirst(Node root){
        Queue<Node> queue = new ArrayDeque<>();
        List<Node> list = new ArrayList<>();
        if(root != null){
            //将根节点加入队列
            queue.offer(root);
        }
        while(!queue.isEmpty()){
            //将队列尾部元素添加到list中
            list.add(queue.peek());
            Node p = queue.poll();
            //若左节点不为null，将左节点加入队列中
            if(p.leftChild != null){
                queue.offer(p.leftChild);
            }
            //若右节点不为null，将右节点加入队列中
            if(p.rightChild != null){
                queue.offer(p.rightChild);
            }
        }
        return list;
    }

    /**
     * 深度优先遍历（先序）
     * ——先处理根节点，再递归遍历左子树，最后递归遍历右子树
     * @param root
     * @return
     */
    public List<Node> preIterator(Node root){
        List<Node> list = new ArrayList<>();
        list.add(root);
        if(root.leftChild != null){
            list.addAll(preIterator(root.leftChild));
        }
        if(root.rightChild != null){
            list.addAll(preIterator(root.rightChild));
        }
        return list;
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[]args){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node("A",20));
        nodes.add(new Node("B",50));
        nodes.add(new Node("C",10));
        nodes.add(new Node("D",45));
        nodes.add(new Node("E",287));
        System.out.println(nodes);
        quickSort(nodes);
        System.out.println(nodes);
    }
}
