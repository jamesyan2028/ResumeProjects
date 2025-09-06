x = double(imread('ima-noisy.pgm'))/255; % Reads the grayscale values of the digital image ima-noisy.pgm
imshow(uint8(x * 255));
title('Original Image');
myepsilon=10^(-2);

for t=5:5:40
 [y_k, niter]=ADMM(x,t,myepsilon);
 norm_y=norm(y_k(:),2);
   fprintf('t = %d, Number of iterations = %d, Norm of y_k = %.4f\n', t, niter, norm_y);
 if (t==20)
    imwrite(uint8(y_k*255), 'denoised.pgm');
    imshow(uint8(y_k*255));
    title('Smoothed Image')
 end
end
