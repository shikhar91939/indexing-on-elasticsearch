package googleInterview;

public class practise04_StringSubsequence {
    public static void main(String[] args) {
        System.out.println(solution("abcd","abd"));
        System.out.println(solution("abcd","abad"));
        System.out.println(solution("abcd",""));
        System.out.println(solution("","a"));
        System.out.println(solution("aaaaa","a"));
    }

    private static int solution(String s, String t) {
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();

        int sIndex = 0;
        int tIndex = 0;
        for (; sIndex < sArr.length && tIndex < tArr.length; sIndex++) {
            if (sArr[sIndex] == tArr[tIndex]) {
                tIndex++;
            }
        }
        if (tIndex != tArr.length /*- 1*/) {
            return 0;
        }
        return 1;
    }
}
