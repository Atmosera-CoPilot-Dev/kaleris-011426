Create a type-safe Python app that uses  numpy, pandas, and matplotlib to generate a set of data points and plot them on a graph. Place the code in the file named 'gendata.py' in this folder.

1) 
Import the required libraries.

2)
Create a function 'gen_data' that generates a set of data points (x, f(x)) and returns them as a pandas data frame.
Arguments:
- 'x_range' is a tuple of two integers representing the range of x values to generate.
Returns:
- A pandas data frame with two columns, 'x' and 'y'.
Details:
- 'x' values are generated randomly between x_range[0] and x_range[1].
- 'y' values are generated as a non-linear function of x with excessive random noise: y = x ^ 1.5  + noise.
- The data frame is sorted by the 'x' values.
Error Handling:
- If x_range is not a tuple of two integers, raise a ValueError.
- If x_range[0] is not less than x_range[1], raise a ValueError.
Example usage:
data = gen_data((0, 100))


3)
Create a function 'gen_data' that generates a set of data points (x, f(x)) and returns them as a pandas data frame.
Arguments:
- 'x_range' is a tuple of two integers representing the range of x values to generate.
Returns:
- A pandas data frame with two columns, 'x' and 'y'.
Details:
- 'x' values are generated randomly between x_range[0] and x_range[1].
- 'y' values are generated as a non-linear function of x with excessive random noise: y = x ^ 1.5  + noise.
- The data frame is sorted by the 'x' values.
Error Handling:
- If x_range is not a tuple of two integers, raise a ValueError.
- If x_range[0] is not less than x_range[1], raise a ValueError.
Example usage:
data = gen_data((0, 100))


4)
Create a function 'plot_data' that plots the data points from the data frame. The copilot response should contain python docstrings for the function.
Arguments:
- 'data' is a pandas data frame with two columns, 'x' and 'y'.
Returns:
- None
Details:
- The data points are plotted as a scatter plot.
- The plot has a title 'Data Points', x-axis label 'x', and y-axis label 'f(x)'.
Error Handling:
- If 'data' is not a pandas data frame with two columns, raise a ValueError.
Example usage:
data = gen_data((0, 100))
plot_data(data)

5)
Create a 'main' function that calls 'gen_data' and 'plot_data' to generate and plot the data points.
Arguments:
    None
Returns:
    None
Example usage:
    data = gen_data((0, 100))
    plot_data(data)

6)
Call the 'main' function to execute the app.


