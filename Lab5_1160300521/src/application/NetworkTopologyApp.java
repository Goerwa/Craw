package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Exception.ExceptionofDirection;
import Exception.ExceptionofInput;
import Exception.ExceptionofUndirection;
import Exception.ExceptionofUnproperEdge;
import edge.CommentTie;
import edge.Edge;
import edge.ForwardTie;
import edge.FriendTie;
import edge.NetworkConnection;
import factory.ComputerVertexFactory;
import factory.ForwardTieFactory;
import factory.FriendTieFactory;
import factory.GraphPoetFactory;
import factory.NetworkConnectionFactory;
import factory.NetworkTopologyFactory;
import factory.PersonVertexFactory;
import factory.RouterVertexFactory;
import factory.ServerVertexFactory;
import factory.WordVertexFactory;
import graph.Graph;
import graph.GraphPoet;
import graph.NetworkTopology;
import vertex.Computer;
import vertex.Person;
import vertex.Router;
import vertex.Server;
import vertex.Vertex;
import vertex.Word;



public class NetworkTopologyApp {

  private void help() {
    System.out.println("vertex --add label type");
    /* vertex --add “l5c1” “Computer” 
       vertex --add “l5r1” “Router”
       vertex --add “l5r2” “Router”
       vertex --add “l5r3” “Router”
       edge --add “e1” “NetworkConnection” weight=Y 2 directed=N “l5c1” “l5r1”*/
    System.out.println("edge --add label type [weighted=Y|N] [weight] [directed=Y|N] v1, v2");
    // edge --add “e1” “NetworkConnection” weight=Y 2 directed=N “l5c1” “l5r1”
    // edge --add “e2” “NetworkConnection” weight=Y 2 directed=N “l5r2” “l5r1”
    // edge --add “e3” “NetworkConnection” weight=Y 2 directed=N “l5r3” “l5r1”
    System.out.println("vertex --delete regex");
    // vertex --delete l5c1
    // vertex --delete l5r1
    // vertex --delete l5r2
    // vertex --delete l5r3
    System.out.println("edge --delete regex");
    // edge --delete e1
    // edge --delete e2
    // edge --delete e3
    // edge --delete c*d*
    System.out.println("hyperedge --add label type vertex1, ..., vertexn");
    // hyperedge --add “ForeverYoung” “SameMovieHyperEdge” “abb” ,“abbb” ,“aabb”
    // hyperedge --add “ForeverYoung” “SameMovieHyperEdge” “abb” ,“aabb”
    // hyperedge --add “ForeverYoung” “SameMovieHyperEdge” “abb”
  }

  private static void printstr(String[] str) {
    for (int i = 0; i < str.length; i++) {
      System.out.println(str[i]);
    }
  }

  private static void justvertex(String str, NetworkTopology graph) throws ExceptionofInput, IOException {
    System.out.println("your input string is:\t" + str);
    str = str.replace("vertex --add", "").replaceAll("”", "").replaceAll("“", "")
        .trim();
    String[] info = str.split(" +");
    printstr(info);
    if (info[1].equals("Computer")) {
      Computer r = (Computer) new Computer(info[0]);
      System.out.println("add");
      graph.addVertex(r);
    } else if (info[1].equals("Router")) {
      Router r = (Router) new Router(info[0]);
      graph.addVertex(r);
    } else if (info[1].equals("Server")) {
      Server r = (Server) new ServerVertexFactory().createVertex(info[0], info);
      graph.addVertex(r);
    } else {

    }
  }

  private static void justedge(String str, NetworkTopology graph)
      throws ExceptionofInput, ExceptionofUnproperEdge, IOException {
    System.out.println("your input string is:\t" + str);
    str = str.replace("edge --add", "").replace("”", "").replace("“", "")
        .replace("=", " ").trim();
    String[] info = str.split(" +");
    //printstr(info);
    Vertex src = graph.findvertexbylabel(info[7]);
    Vertex tar = graph.findvertexbylabel(info[8]);
    List<Vertex> vers = new ArrayList<>();
    vers.add(tar);
    vers.add(src);
    double weight = Double.parseDouble(info[4]);
    if (info[1].equals("NetworkConnection")) {
      NetworkConnection r =
          (NetworkConnection) new NetworkConnectionFactory().createEdge(info[0], vers, weight);
      graph.addEdge(r);
    } else {

    }
  }

  private static void justremovev(String str, NetworkTopology graph) {
    System.out.println("your input string is:\t" + str);
    System.out.println(graph.vertices().size());
    String r = str.replace("vertex --delete", "").replaceAll(" ", "");
    System.out.println(r);
    Pattern v = Pattern.compile(r);
    Set<Vertex> vertices = graph.vertices();
    for (Vertex sv : vertices) {
      Matcher vertex = v.matcher(sv.getlabel());
      if (vertex.matches()) {
        graph.removeVertex(sv);
      }
    }
    System.out.println(graph.vertices().size());
  }

  private static void justremovee(String str, NetworkTopology graph)
      throws ExceptionofInput, ExceptionofUnproperEdge, IOException {
    System.out.println("your input string is:\t" + str);
    System.out.println(graph.edges().size());
    String r = str.replace("edge --delete", "").replaceAll(" ", "");
    System.out.println(r);
    Pattern v = Pattern.compile(r);
    Set<Edge> edges = graph.edges();
    for (Edge e : edges) {
      Matcher edgem = v.matcher(e.getlabel());
      if (edgem.matches()) {
        graph.removeEdge(e);
      }
    }
    System.out.println(graph.edges().size());
  }

  private static void justhyper(String str, Graph graph) {
    System.out.println("your input string is:\t" + str);
    String r = str.replace("hyperedge --add", "").replace(",", "").replace("“", "").replace("”", "")
        .trim();
    String[] info = r.split(" ");
    for (int i = 0; i < info.length; i++) {
      System.out.println(info[i]);
    }
    System.out.println(r);
  }

  public static void main(String[] args)
      throws IOException, ExceptionofInput, ExceptionofUnproperEdge, ExceptionofUndirection, ExceptionofDirection {
    System.out.println("选择读取文件方式");
    System.out.println("1.read file by Stream");
    System.out.println("2.read file by Reader");
    System.out.println("3.read file by Buffer");
    System.out.println("4.read file by Scanner");
    System.out.println("5.read file by java.nio.file.Files");
    Scanner it = new Scanner(System.in);
    int n = it.nextInt();
    NetworkTopology graph =
        (NetworkTopology) new NetworkTopologyFactory().createGraph("src/source/file1.txt", n);
    // build("src/source/test1.txt",graph);
    // System.out.println(graph.vertices().size());
    // System.out.println(graph.edges().size());
    Pattern vertexp = Pattern.compile("vertex --add(\\s+“[A-Za-z0-9]+”\\s*){2}");
    Pattern edgep = Pattern.compile(
        "edge --add(\\s+“[A-Za-z0-9]+”\\s*){2}(\\s*weight=(Y|N)\\s+[0-9])(\\s*directed=(Y|N))(\\s+“[A-Za-z0-9]+”\\s*){2}");
    Pattern removev = Pattern.compile("vertex --delete[\\s\\S]*");
    Pattern removee = Pattern.compile("edge --delete[\\s\\S]*");
    Pattern hyperedge =
        Pattern.compile("hyperedge --add(\\s*“[A-Za-z0-9]+”\\s*){2}(\\s*“[A-Za-z0-9]+”\\s*,*)+");
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    String str = "run";
    while (!str.equals("exit")) {
      System.out.println("\tinpur your order(can use help to show all orders)");
      str = stdin.readLine();
      Matcher vertexm = vertexp.matcher(str);
      Matcher edgem = edgep.matcher(str);
      Matcher removevm = removev.matcher(str);
      Matcher removeem = removee.matcher(str);
      Matcher hyperm = hyperedge.matcher(str);
      if (vertexm.matches()) {
        justvertex(str, graph);
        System.out.println(graph.vertices().size());
      }
      // Matcher edgem = edgep.matcher(str);
      else if (edgem.matches()) {
        System.out.println(graph.edges().size());
        justedge(str, graph);
        System.out.println(graph.edges().size());
      }
      // Matcher removevm = removev.matcher(str);
      else if (removevm.matches()) {
        System.out.println(str);
        justremovev(str, graph);

      }
      // Matcher removeem = removee.matcher(str);
      else if (removeem.matches()) {
        System.out.println(str);
        justremovee(str, graph);

      }
      // Matcher hyperm = hyperedge.matcher(str);
      else if (hyperm.matches()) {
        System.out.println(str);
        justhyper(str, graph);
      } else {
        System.out.println("faile");
      }
    }
    System.out.println("exit");
  }

}
