import math
import numpy as np
import tensorflow as tf

@tf.keras.saving.register_keras_serializable(package="transformer_layers")
class AttentionMatrix(tf.keras.layers.Layer):

    def __init__(self, *args, use_mask=False, **kwargs):
        super().__init__(*args, **kwargs)
        # Mask is [batch_size x window_size_queries x window_size_keys]

        self.use_mask = use_mask

    def call(self, inputs):
        """
        STUDENT MUST WRITE:

        This functions runs a single attention head.

        :param K: is [batch_size x window_size_keys x embedding_size]
        :param Q: is [batch_size x window_size_queries x embedding_size]
        :return: attention matrix
        """
        K, Q = inputs
        window_size_queries = Q.get_shape()[1]  # window size of queries
        window_size_keys    = K.get_shape()[1]  # window size of keys
        embedding_size_keys = K.get_shape()[2]

        mask = tf.convert_to_tensor(
            value=np.transpose(np.tril(np.ones((window_size_queries, window_size_keys)) * np.NINF, -1), (1, 0)),
            dtype=tf.float32)
        atten_mask = tf.tile(tf.reshape(mask, [-1, window_size_queries, window_size_keys]), [tf.shape(input=K)[0], 1, 1])

        # TODO:
        # 1) compute self-attention weights (if use_mask==True, make sure to add the attention mask before softmax)
        # 2) return the attention matrix

        score = tf.matmul(Q, tf.transpose(K, perm = [0, 2, 1])) / tf.sqrt(tf.cast(embedding_size_keys, tf.float32))
        if self.use_mask == True:
            score = score + atten_mask
        weights = tf.nn.softmax(score, axis = -1)
        return weights 

@tf.keras.saving.register_keras_serializable(package="transformer_layers")
class AttentionHead(tf.keras.layers.Layer):
    def __init__(self, input_size, output_size, is_self_attention, **kwargs):
        super(AttentionHead, self).__init__(**kwargs)
        self.use_mask = is_self_attention

        # TODO:
        # Initialize the attention matrices (what size tensor should they produce?)
        self.K = self.add_weight(shape = (input_size, output_size), trainable = True, initializer='glorot_uniform', name = "key_weights")
        self.V = self.add_weight(shape = (input_size, output_size), trainable = True, initializer='glorot_uniform', name = "value_weights")
        self.Q = self.add_weight(shape = (input_size, output_size), trainable = True, initializer='glorot_uniform', name = "query_weights")
        self.attention = AttentionMatrix(use_mask = is_self_attention)

        

    @tf.function
    def call(self, inputs_for_keys, inputs_for_values, inputs_for_queries):
        """
        This functions runs a single attention head.

        :param inputs_for_keys: tensor of [batch_size x KEY_WINDOW_SIZE x input_size ]
        :param inputs_for_values: tensor of [batch_size x KEY_WINDOW_SIZE x input_size ]
        :param inputs_for_queries: tensor of [batch_size x QUERY_WINDOW_SIZE x input_size ]
        :return: tensor of [BATCH_SIZE x QUERY_WINDOW_SIZE x output_size ]
        """

        # TODO
        keys = tf.matmul(inputs_for_keys, self.K)
        values = tf.matmul(inputs_for_values, self.V)
        queries = tf.matmul(inputs_for_queries, self.Q)
        attention_scores = self.attention([keys, queries])
        return tf.matmul(attention_scores, values)

@tf.keras.saving.register_keras_serializable(package="transformer_layers")
class MultiHeadedAttention(tf.keras.layers.Layer):
    def __init__(self, emb_sz, use_mask, **kwargs):
        super(MultiHeadedAttention, self).__init__(**kwargs)

        # Initialize Attention Heads Here
        self.attention_head_1 = AttentionHead(emb_sz, emb_sz // 3, is_self_attention = use_mask)
        self.attention_head_2 = AttentionHead(emb_sz, emb_sz // 3, is_self_attention = use_mask)
        self.attention_head_3 = AttentionHead(emb_sz, emb_sz // 3, is_self_attention = use_mask)
        self.correct_resize = tf.keras.layers.Dense(emb_sz)

        

    @tf.function
    def call(self, inputs_for_keys, inputs_for_values, inputs_for_queries):
        """
        This functions runs a multiheaded attention layer.

        Requirements:
            - 3 different heads of size embed_sz/3

        :param inputs_for_keys: tensor of [batch_size x KEY_WINDOW_SIZE x input_size ]
        :param inputs_for_values: tensor of [batch_size x KEY_WINDOW_SIZE x input_size ]
        :param inputs_for_queries: tensor of [batch_size x QUERY_WINDOW_SIZE x input_size ]
        :return: tensor of [BATCH_SIZE x QUERY_WINDOW_SIZE x output_size ]
        """
        head_1 = self.attention_head_1(inputs_for_keys, inputs_for_values, inputs_for_queries)
        head_2 = self.attention_head_2(inputs_for_keys, inputs_for_values, inputs_for_queries)
        head_3 = self.attention_head_3(inputs_for_keys, inputs_for_values, inputs_for_queries)
        curr = tf.concat([head_1, head_2, head_3], axis=-1)
        return self.correct_resize(curr)



@tf.keras.saving.register_keras_serializable(package="transformer_layers")
class TransformerBlock(tf.keras.layers.Layer):
    def __init__(self, emb_sz, multiheaded=False, **kwargs):
        super(TransformerBlock, self).__init__(**kwargs)

        # TODO
        # Use multiheaded attention if multiheaded is True!
        if multiheaded == True:
            self.self_attention = MultiHeadedAttention(emb_sz = emb_sz, use_mask = True)
            self.cross_attention = MultiHeadedAttention(emb_sz = emb_sz, use_mask = False)
        else:
            self.self_attention = AttentionHead(input_size= emb_sz, output_size = emb_sz, is_self_attention = True)
            self.cross_attention = AttentionHead(input_size= emb_sz, output_size = emb_sz, is_self_attention = False)
        
        self.norm1 = tf.keras.layers.LayerNormalization()
        self.norm2 = tf.keras.layers.LayerNormalization()

        self.fc1 = tf.keras.layers.Dense(4 * emb_sz, activation = "relu")
        self.fc2 = tf.keras.layers.Dense(emb_sz)

        self.norm3 = tf.keras.layers.LayerNormalization()

        

    @tf.function
    def call(self, inputs, context_sequence):
        """
        This functions calls a transformer block.

        :param inputs: tensor of shape [BATCH_SIZE x INPUT_SEQ_LENGTH x EMBEDDING_SIZE ]
        :param context_sequence: tensor of shape [BATCH_SIZE x CONTEXT_SEQ_LENGTH x EMBEDDING_SIZE ]
        :return: tensor of shape [BATCH_SIZE x INPUT_SEQ_LENGTH x EMBEDDING_SIZE ]
        """

        # TODO
        self_attention = self.self_attention(inputs, inputs, inputs)
        norm1 = self.norm1(self_attention + inputs)

        cross_attention = self.cross_attention(context_sequence, context_sequence, norm1)
        norm2 = self.norm2(norm1 + cross_attention)

        fc1_output = self.fc1(norm2)
        fc2_output = self.fc2(fc1_output)

        norm3 = self.norm3(norm2 + fc2_output)

        return norm3



@tf.keras.saving.register_keras_serializable(package="transformer_layers", name="positional_encoding")
def positional_encoding(length, depth):
    ## TODO:
    def get_angles(pos, i, d_model):
        angle_rates = 1 / np.power(10000, (2 * (i//2)) / np.float32(d_model))
        return pos * angle_rates

    angle_rads = get_angles(np.arange(length)[:, np.newaxis], np.arange(depth)[np.newaxis, :], depth)

    sin_angles = np.sin(angle_rads[:, 0::2])
    
    cos_angles = np.cos(angle_rads[:, 1::2])

    pos_encoding = np.concatenate([sin_angles, cos_angles], axis = -1)
        
    pos_encoding = pos_encoding[np.newaxis, ...]
        
    return pos_encoding


@tf.keras.saving.register_keras_serializable(package="transformer_layers")
class PositionalEncoding(tf.keras.layers.Layer):
    def __init__(self, vocab_size, embed_size, window_size):
        super().__init__()
        self.embed_size = embed_size
        self.embedding = tf.keras.layers.Embedding(vocab_size, embed_size, mask_zero=True)

        ## Sinosoidal positional encoding: offset by varying sinosoidal frequencies.
        ## HINT: https://www.tensorflow.org/text/tutorials/transformer#the_embedding_and_positional_encoding_layer
        self.pos_encoding = positional_encoding(length=window_size, depth=embed_size)[..., :window_size, :]

    def call(self, x):
        ## TODO: Get embeddings and and scale them by sqrt of embedding size, and add positional encoding.
        embeddings = self.embedding(x)
        embeddings = embeddings * tf.sqrt(tf.cast(self.embed_size, tf.float32))
        embeddings += self.pos_encoding
        return embeddings
