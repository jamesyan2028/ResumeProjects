This project smoothes noisy .pgm image files. The input file path, as well as the desired stopping condition is modified in runADMM.m. The stopping condition is a measure
of how smooth the final output image should be, and is quantified by choosing some threshold epsilon. The stopping condition says that if the difference between our current 
guess of the smoothed image and our previous guess is small enough, then we are close enough to the true smoothest image and can return our guess. More technically, when the 
Euclidean norm of the difference between two consecutive vectorized image outputs from the ADMM algorithm falls under this threshold, we can return our current iteration. 
