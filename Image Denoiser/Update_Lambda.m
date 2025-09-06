function [lambda_bottom_kp1,lambda_right_kp1] = Update_Lambda(lambda_bottom_k,lambda_right_k,d_bottom_k,d_right_k,y_k)
[M, N] = size(y_k);
for i=1:M-1
    for j=1:N
        lambda_bottom_kp1(i,j)=lambda_bottom_k(i,j)+d_bottom_k(i,j)-(y_k(i+1,j)-y_k(i,j));
    end
end
for i=1:M
    for j=1:N-1
        lambda_right_kp1(i,j)=lambda_right_k(i,j)+d_right_k(i,j)-(y_k(i,j+1)-y_k(i,j));
    end
end

end
