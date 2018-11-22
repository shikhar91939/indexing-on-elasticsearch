package googleInterview;

import java.util.HashMap;

public class domains {
    public int solution(String[] L) {
        HashMap<String,Integer> map = new HashMap<>();
        StringBuilder filter = new StringBuilder();
        String[] g = new String[2];
        int count = 0;
        for (String s : L) {
            g = s.split("@");
            for (int i = 0; i < g[0].length(); i++) {
                if (g[0].charAt(i) == '+') {
                    break;
                }
                if (g[0].charAt(i) != '.') {
                    filter.append(g[0].charAt(i));
                }
            }
            filter.append("@");
            filter.append(g[1]);
            // System.out.println(filter.toString());
            if(map.containsKey(filter.toString())){
                map.put(filter.toString(),map.get(filter.toString())+1);
            }
            else{
                map.put(filter.toString(),1);
            }
            filter.setLength(0);
        }
//        for(Integer v : map.values()){
//            if(v>1){
//                count++;
//            }
//        }
        return (int) map.entrySet().stream().filter(e -> e.getValue() >= 2).count();

    }

    public static void main(String[] args) {
        domains d = new domains();
        System.out.println(d.solution(new String[] {"a.b@example.com", "a.b+work@example.com", "ab+abx@example.com"}));
        System.out.println(d.solution(new String[] {"a.b@example.com", "a.b+work@example.com", "ab+abx@examplessss.com", "a.b+abx@examplessss.com"}));
        System.out.println(d.solution(new String[] {"za.b@example.com", "za.b+work@example.com", "za.be@xample.com", "za.be@xample.com"}));
        System.out.println(d.solution(new String[] {"a.b@example.com","a.b@example.com","a.b@example.com","a.b@example.com"}));
    }

    /*
    ['a.b@example.com', 'a.b+work@example.com', 'ab+abx@example.com'] = 1
    ['a.b@example.com', 'a.b+work@example.com', 'ab+abx@examplessss.com', 'a.b+abx@examplessss.com'] = 2
    ['za.b@example.com', 'za.b+work@example.com', 'za.be@xample.com', 'za.be@xample.com'] = 2
    ['a.b@example.com','a.b@example.com','a.b@example.com','a.b@example.com'] =1
    */
}
