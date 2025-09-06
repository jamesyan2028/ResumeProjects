 
function [y_k,niter] = ADMM(x,t, myepsilon)
  k=0;  %Initializes the number of iterations to 0.
  [M,N] = size(x); %Takes into account the rows and columns of the given matrices.
  y_k = x; %Initializes y_k to the picture. Y_k is a sequence value we will update.
  d_k = zeros(M,N); %Initializes d_k to a matrix of zeros.
  l_k = zeros(M,N); %Initializes l_k to a matrix of zeros.
  lambda_down_k = zeros(M-1, N); %Initializes lambda_down.
  lambda_right_k = zeros(M,N-1); %Initializes lambda_right.
  d_down_k = zeros(M-1,N); %Initializes d_down.
  d_right_k = zeros(M,N-1); %Initializes d_right.

  shall_continue = true;
  while shall_continue == true
    k = k + 1;
    y_temp=y_k;
    y_k=Update_Y(y_k, lambda_down_k, lambda_right_k, d_down_k, d_right_k,x,t);
    [d_down_k, d_right_k]=Update_D(lambda_down_k, lambda_right_k, y_k);
    [lambda_down_k, lambda_right_k]=Update_Lambda(lambda_down_k, lambda_right_k, d_down_k, d_right_k, y_k);
    if norm(y_k(:)-y_temp(:),2)<=myepsilon
        shall_continue=false;
    end
  end % end while
  niter = k; %Sets the final number of iterations.

end

