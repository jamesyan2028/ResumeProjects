function [y_p1] = Update_Y(y,lambda_down_k,lambda_right_k,d_down_k,d_right_k,x,t)
[M,N] = size(x);

y_p1=y;
for k=1:15
    for i=1:M
        for j=1:N
            %y_k boundary values
            nb_neighbors=0;
            y_im1_j=0;
            if i>1
                y_im1_j=y_p1(i-1,j);
                nb_neighbors=nb_neighbors+1;
            end
            y_ip1_j=0;
            if i<M
                y_ip1_j=y_p1(i+1, j);
                nb_neighbors=nb_neighbors+1;
            end
            y_i_jm1=0;
            if j>1
                y_i_jm1=y_p1(i, j-1);
                nb_neighbors=nb_neighbors+1;
            end
            y_i_jp1=0;
            if j<N
                y_i_jp1=y_p1(i, j+1);
                nb_neighbors=nb_neighbors+1;
            end

            %d_right and d_down boundaries
            dr_i_j=0;
            if j<N
                dr_i_j=d_right_k(i,j);
            end
            dd_i_j=0;
            if i<M
                dd_i_j=d_down_k(i,j);
            end
            dr_i_jm1=0;
            if j>1
                dr_i_jm1=d_right_k(i, j-1);
            end
            dd_im1_j=0;
            if i>1
                dd_im1_j=d_down_k(i-1,j);
            end
                
            %Lambda boundaries
            lr_i_j=0;
            if j<N
                lr_i_j=lambda_right_k(i,j);
            end
            ld_i_j=0;
            if i<M
                ld_i_j=lambda_down_k(i,j);
            end
            lr_i_jm1=0;
            if j>1
                lr_i_jm1=lambda_right_k(i, j-1);
            end
            ld_im1_j=0;
            if i>1
                ld_im1_j=lambda_down_k(i-1,j);
            end
            %Computations
            tmp_y=y_im1_j+y_ip1_j+y_i_jm1+y_i_jp1;
            tmp_d=dr_i_jm1-dr_i_j+dd_im1_j-dd_i_j;
            tmp_l=lr_i_jm1-lr_i_j+ld_im1_j-ld_i_j;
            tmp=t*x(i,j)+tmp_y+(tmp_l+tmp_d);
            tmp=tmp/(t+nb_neighbors);
            y_p1(i,j)=tmp;
        end
    end
end
          
                
                
