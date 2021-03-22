package Project2;

import java.util.ArrayList;

public class PQ {
    public Node root;

    public PQ(int kArity)
    {
        Node.maxNrOfChildren=kArity;
    }

    public void insert(Event info)
    {
        root=new Node(info);
        root.parent=null;
        root.children=new ArrayList<Node>(Node.maxNrOfChildren);
    }

    public int numberOfNodesInTree(Node rootNode){
        int count=0;

        count++;
        if(rootNode.children.size()!=0) {
            for(Node ch : rootNode.children)
                count=count+numberOfNodesInTree(ch);
        }

        return count;
    }

    public int numberOfNodesInTree()
    {
        return numberOfNodesInTree(this.root);
    }



    public boolean isEmpty() {
        return numberOfNodesInTree() <= 0;
    }

    public Event deleteMin() {
       Event e = new Event();
       e = root.info;
       return e;
  }

}
