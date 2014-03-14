
/**
 * following the function comments for main(), above, *eptr=encoded should effect that a new
 * pointer which points to type char is assigned the value of (the address? Rvalue?) of encoded
 */
int cgi_decode(char *encoded,char *decoded){
	char *eptr = encoded; // remember params are passed by value in C, so before this line *eptr="", after exec, eptr=ENCODED_PARAM_VALUE(rvalue)
	char *dptr = decoded;
	int ok=0;
	printf("val=%s \n", encoded);

	while(*eptr){ //same as .. "while(RValue of eptr[i]!=null)"
		char c;
		c=*eptr;
		if(c=='+'){
			*dptr=' ';
		}
		else if(c =='%'){
			++eptr; // in C char has same size as int (sizeof(int/char)===1) so eptr++ means incremented memory addr (1 byte/4 bits away)
			++eptr;
			int digit_high = 0;//Hex_Values[*(++eptr)]; // i take this to mean assign value of eptr-Rvalue to digit_high then assign incremented eptr's-rValue to eptr-pointer (the pointer points to eptr-Rvalue-incremented's location still IOW)
			int digit_low = 16;// the above all makes sense with pointer arithmetic and explanations since 1 byte can range 0-16 //Hex_Values[*(++eptr)];
			if(digit_high == -1 || digit_low== -1){ // since char treated like int and int==1byte, max (pos. signed) is 127 by rule of two's complement for bits, ie: 127==>0111 1111 (-1 ==>1111 1111)
				ok=1;
			}
			else{
				*dptr = 16*digit_high + digit_low; // dptr now points to the next char up in the stack's address
			}
		}
		else{
			*dptr = *eptr;
		}
		++dptr; // [5]
		++eptr;
	}
	*dptr = '\0'; // set dptr to the null character [6]
	char dp=*dptr;
	printf("\n&dptr:%s \n",&dp);

	return ok;
}


/**
 * WHAT IS HAPPENING WHEN THE CODE ABOVE RUNS?:
 *
 *
 * ++eptr;
 * [5] http://pw1.netcom.com/~tjensen/ptr/ch2x.htm
 * [5] Similarly, since ++ptr and ptr++ are both equivalent to ptr + 1 (though the point in the program when ptr is incremented may be different), incrementing a pointer using the unary ++ operator, either pre- or post-, increments the address it stores by the amount sizeof(type) where "type" is the type of the object pointed to. (i.e. 4 for an integer).
 *
 * dptr='\0';
 * [6] http://pw1.netcom.com/~tjensen/ptr/ch3x.htm
 * [6] While the character pointed to by pA (i.e. *pA) is not a nul character (i.e. the terminating '\0'), do the following:
 */
