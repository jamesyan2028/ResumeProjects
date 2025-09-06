function out = Prox_Absolute_Value(z)
    out = 0;
    if z >= 1
        out=z-1;
    end 
    if z<= -1
        out=z+1;
    end
end
