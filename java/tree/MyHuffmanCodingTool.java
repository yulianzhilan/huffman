package tree;

import java.nio.charset.Charset;
import java.util.*;

/**
 * 此工具类仅供学习huffman编码解码使用
 * Created by scott on 2017/4/12.
 */
public class MyHuffmanCodingTool {

    /**
     * 二叉树的三叉链表储存
     */
    static class Node implements Comparable<Node>{
        //数据
        private Character data;
        //频度
        private int frequency = 0;
        //父节点
        private Node parent;
        //左子节点
        private Node leftChild;
        //右子节点
        private Node rightChild;

        @Override
        public int compareTo(Node o) {
            return this.frequency-o.frequency;
        }

        public boolean isLeaf(){
            return null == leftChild && null == rightChild;
        }

        public boolean isRoot(){
            return null == parent;
        }

        public boolean isLeftChild(){
            return parent != null && parent.leftChild == this;
        }

        public Character getData() {
            return data;
        }

        public void setData(Character data) {
            this.data = data;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }

    /**
     * 计算字符数组的字符组成和频度
     * @param chars
     * @return
     */
    private static Map<Character, Integer> countWeight(char[] chars){
        Map<Character, Integer> map = new HashMap<>();
        for(char c : chars){
            if(map.containsKey(c)){
                map.put(c, map.get(c) + 1);
            } else{
                map.put(c, 1);
            }
        }
        return map;
    }

    /**
     * 生成树
     * @param stand
     * @param leaves
     * @return
     */
    private static Node buildTree(Map<Character, Integer> stand, List<Node> leaves){
        Character[] keys = stand.keySet().toArray(new Character[0]);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for(Character c : keys){
            Node node = new Node();
            node.data = c;
            node.frequency = stand.get(c);
            priorityQueue.add(node);
            if(leaves != null){
                leaves.add(node);
            }
        }
        int size = priorityQueue.size();
        for(int i = 1; i <= size - 1; i++){
            Node node1 = priorityQueue.poll();
            Node node2 = priorityQueue.poll();

            Node parent = new Node();
            parent.frequency = node1.frequency + node2.frequency;

            parent.leftChild = node1;
            parent.rightChild = node2;

            node1.parent = parent;
            node2.parent = parent;

            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }

    /**
     * 生成简码
     * @param leaves
     * @return
     */
    private static Map<Character, String> calculateCode(List<Node> leaves){
        Map<Character, String> map = new HashMap<>();

        for(Node node : leaves){

            Node currentNode = node;
            StringBuffer code = new StringBuffer();
            do{
                if(currentNode.isLeftChild()){
                    code.append("0");
                } else{
                    code.append("1");
                }
                currentNode = currentNode.parent;
            } while(!currentNode.isRoot());
            map.put(node.data, code.reverse().toString());
        }
        return map;
    }

    /**
     * huffman编码
     * @param origin
     * @param stand
     * @return
     */
    public static String encode(String origin,Map<Character, Integer> stand){
        if(origin == null || "".equals(origin)){
            return "";
        }
        char[] characters = origin.toCharArray();
        stand.putAll(countWeight(characters));
        List<Node> leaves = new ArrayList<>();
        buildTree(stand, leaves);
        Map<Character, String> code = calculateCode(leaves);
        StringBuffer gen = new StringBuffer();
        for(Character c : characters){
            gen.append(code.get(c));
        }
        return gen.toString();
    }

    /**
     * 字符集编码
     * @param origin
     * @param charset
     * @return
     */
    public static String encode(String origin, String charset) {
        if(charset == null || "".equals(charset)|| origin == null || "".equals(origin)){
            return "";
        }
        Charset set = Charset.forName(charset);
        byte [] origins = origin.getBytes(set);
        StringBuffer result = new StringBuffer();
        for(byte b : origins){
            result.append(getStringOfByte(b));
        }
        return result.toString();
    }

    /**
     * 十进制转换为而二进制
     * 类似的可以使用
     * Integer.toBinaryString()
     * 注意：要是判断长度最后进行补0
     * @param b
     * @return
     */
    private static String getStringOfByte(byte b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            byte temp = (byte) ((b >> i) & 0x1);
            buffer.append(String.valueOf(temp));
        }
        return buffer.toString();
    }

    public static String decode(String origin, Map<Character, Integer> stand){
        Node root = buildTree(stand, null);
        char[] origins = origin.toCharArray();
        LinkedList<Character> list = new LinkedList<>();
        int size = origins.length;

        for(int i = 0; i < size; i++){
            list.addLast(origins[i]);
        }

        StringBuffer result = new StringBuffer();

        while(list.size()>0){
            Node node = root;
            do{
                Character c = list.removeFirst();
                if('0' == c){
                    node = node.leftChild;
                } else if('1' == c){
                    node = node.rightChild;
                }
            } while(!node.isLeaf());
            result.append(node.data);
        }
        return result.toString();
    }


    public static void main(String[] args) {
        System.out.println("welcome to using huffman encoding tools.");
        System.out.println("please input message:(end with bye)");
        Scanner in = new Scanner(System.in);
        String origin = in.nextLine();
        while(!"bye".equals(origin)){
            Map<Character,Integer> stand = new HashMap<>();
            String code = encode(origin, stand);
            System.out.println("huffman encode: " + code);
            System.out.println("utf-8 encode: " + encode(origin,"utf-8"));
            System.out.println("gbk encode: " + encode(origin, "gbk"));
            System.out.println("huffman decode:" + decode(code, stand));
            origin = in.nextLine();
        }
        System.out.println("Bye-Bye!");
    }
}
