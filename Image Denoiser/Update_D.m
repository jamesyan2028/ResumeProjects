function [d_down_kp1, d_right_kp1] = Update_D(lambda_down_k, lambda_right_k, y_k)
[M,N] = size(y_k); %Takes into account the rows and columns of the given matrices.
for i=1:M-1
    for j=1:N
        d_down_kp1(i,j)=Prox_Absolute_Value(y_k(i+1,j)-y_k(i,j)-lambda_down_k(i,j));
    end
end
for i=1:M
    for j=1:N-1
        d_right_kp1(i,j)=Prox_Absolute_Value(y_k(i,j+1)-y_k(i,j)-lambda_right_k(i,j));
    end
end
end
