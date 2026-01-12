Use Copilot Chat 'Ask','Edit', or 'Agent' mode for the following prompts:

1) Add a method 'comb' to generate the count of combinations.

2) Refactor the revised class to make use of memoization.

3) Add xml documentation to the class and all methods.

4) Translate the class into C++. I want the implementation separate from the definition.

``` CSharp
/*
This file contains a class that provides operations in the mathematical field of combinatorics.
*/

namespace MathFun
{
    /// <summary>
    /// Provides operations in the mathematical field of combinatorics, including factorial, permutation, and combination calculations.
    /// </summary>
    public class Combinatorics
    {
        /*
        define a public method named 'fact' that takes an unsigned integer 'n' and returns the factorial of 'n' using recursion:
        Implementation details:

        if n is less than or equal to 1, return 1
        return n * fact(n - 1)
        */
        /// <summary>
        /// Dictionary to cache computed factorials for memoization.
        /// </summary>
        private static System.Collections.Generic.Dictionary<uint, ulong> factCache = new System.Collections.Generic.Dictionary<uint, ulong>();

        /// <summary>
        /// Calculates the factorial of a non-negative integer n using recursion and memoization.
        /// </summary>
        /// <param name="n">The non-negative integer for which to calculate the factorial.</param>
        /// <returns>The factorial of n as an unsigned long integer.</returns>
        public static ulong fact(uint n)
        {
            if (factCache.ContainsKey(n))
            {
                return factCache[n];
            }
            ulong result;
            if (n <= 1)
            {
                result = 1;
            }
            else
            {
                result = n * fact(n - 1);
            }
            factCache[n] = result;
            return result;
        }

        /// <summary>
        /// Calculates the number of permutations of n items taken r at a time.
        /// </summary>
        /// <param name="n">The total number of items.</param>
        /// <param name="r">The number of items to arrange.</param>
        /// <returns>The number of permutations as an unsigned long integer.</returns>
        public static ulong perm(uint n, uint r)
        {
            return fact(n) / fact(n - r);
        }


        /// <summary>
        /// Calculates the number of combinations of n items taken r at a time.
        /// </summary>
        /// <param name="n">The total number of items.</param>
        /// <param name="r">The number of items to choose.</param>
        /// <returns>The number of combinations as an unsigned long integer.</returns>
        public static ulong comb(uint n, uint r)
        {
            return fact(n) / (fact(r) * fact(n - r));
        }

  
    }

}

```


