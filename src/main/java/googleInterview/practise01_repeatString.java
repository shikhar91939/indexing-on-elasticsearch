package googleInterview;

/*
* https://www.geeksforgeeks.org/google-interview-experience-set-7-software-engineering-intern/
*
*  Given a string A consisting of n characters and a string B consisting of m characters,
*  write a function that will return the number of times A must be stated
*  such that B is a substring of the repeated A. If B can never be a substring, return -1.
*
*  Example:
*  A = ‘abcd’
*  B = ‘cdabcdab’
*  The function should return 3 because after stating A 3 times, getting ‘abcdabcdabcd’, B is now a substring of A.
*
*  You can assume that n and m are integers in the range [1, 1000].
* */
public class practise01_repeatString {

    public static void main(String[] args) {
        String a = "abcd";
        String b = "cdabcdab";

        System.out.println("answer: 3\t"+answerMethod(a, b)+": actual");
        System.out.println("answer: 1\t"+answerMethod(a, a)+": actual");
        System.out.println("answer: 1\t"+answerMethod("abcd", "d")+": actual");
        System.out.println("answer: 2\t"+answerMethod("abcd", "cda")+": actual");
        System.out.println("answer: -1\t"+answerMethod("abcd", "cdb")+": actual");
        System.out.println("answer: 0\t"+answerMethod("","")+": actual");
        System.out.println("answer: 0\t"+answerMethod("asdf","")+": actual");
        System.out.println("answer: -1\t"+answerMethod("","asdf")+": actual");
    }

    private static int answerMethod(String a, String b) {
        int bLength = b.length();
        int aLength = a.length();

        if (bLength == 0) {
            return 0;
        } else if (aLength == 0) {
            return -1;
        }

        int minTimes;
        int maxTimes;

        if (bLength > aLength) {
            minTimes = (bLength / aLength) - 1;
            maxTimes = ((bLength / aLength) + 1) * 2;
        } else {
            // if (bLength<=aLength)
            minTimes = 0;
            maxTimes = 3;
        }

        StringBuilder s = new StringBuilder();
        int answer = minTimes;
        for (int i = 0; i < minTimes; i++) {
            s.append(a);
        }
        while (answer <= maxTimes) {
            if (s.toString().contains(b)) {
                return answer;
            }
            s.append(a);
            answer += 1;
        }
        return -1;
    }

}
