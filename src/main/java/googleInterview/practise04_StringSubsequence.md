*source: https://raw.githubusercontent.com/ys2843/front-end-interview-materials/master/Google%20OA%20New.md*

#### String Subsequence

Write a function that, given a string S and a string T, return 1 if it's possible to convert string S into string T by deleting some(possible zero) characters from string S, and otherwise returns 0

For example, given S="abcd" and T="abd" the function should return 1. We can delete 'c' from string S to convert string S into string T. However, given S="ab" and T="ba" the function should return 0

Assume that:

- the length of ('S' , 'T') is within the range [1..1,000]
- strings S and T consist only of lower-case letters (a-z).

```javascript
function Gsubstring (S, T) {
  for (let i = 0; i < T.length;i++) {
    var index = S.indexOf(T[i]);
    if (index == -1) {
      return 0;
    }
    S = S.slice(index+1);
  }
  return 1;
}
```


