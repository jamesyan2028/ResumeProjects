import tensorflow as tf

try: from transformer import TransformerBlock, PositionalEncoding
except Exception as e: print(f"TransformerDecoder Might Not Work, as components failed to import:\n{e}")

########################################################################################

@tf.keras.saving.register_keras_serializable(package="MyLayers")
class RNNDecoder(tf.keras.layers.Layer):

    def __init__(self, vocab_size, hidden_size, window_size, **kwargs):

        super().__init__(**kwargs)
        self.vocab_size  = vocab_size
        self.hidden_size = hidden_size
        self.window_size = window_size

        # TODO:
        # Now we will define image and word embedding, decoder, and classification layers


        self.embeddings = tf.keras.layers.Embedding(input_dim = self.vocab_size, output_dim = self.hidden_size)
        self.gru = tf.keras.layers.GRU(units = self.hidden_size, return_sequences = True, return_state = False)
        self.dense = tf.keras.layers.Dense(units = self.vocab_size)
        self.image_resize = tf.keras.layers.Dense(units = self.hidden_size)

        
    def call(self, encoded_images, captions):
        """
        :param encoded_images: tensor of shape [BATCH_SIZE x 2048]
        :param captions: tensor of shape [BATCH_SIZE x WINDOW_SIZE]
        :return: batch logits of shape [BATCH_SIZE x WINDOW_SIZE x VOCAB_SIZE]
        """

        # TODO:

        image_resize = self.image_resize(encoded_images)
        caption_embeddings = self.embeddings(captions)
        output = self.gru(caption_embeddings, initial_state = image_resize)
        logits = self.dense(output)
        return logits

    def get_config(self):
        base_config = super().get_config()
        config = {k:getattr(self, k) for k in ["vocab_size", "hidden_size", "window_size"]}
        return {**base_config, **config}

    @classmethod
    def from_config(cls, config):
        return cls(**config)

########################################################################################

@tf.keras.saving.register_keras_serializable(package="MyLayers")
class TransformerDecoder(tf.keras.Model):

    def __init__(self, vocab_size, hidden_size, window_size, **kwargs):

        super().__init__(**kwargs)
        self.vocab_size  = vocab_size
        self.hidden_size = hidden_size
        self.window_size = window_size

        # TODO:
        # Now we will define image and word embedding, positional encoding, tramnsformer decoder, and classification layers
        self.pos_encoding = PositionalEncoding(self.vocab_size, self.hidden_size, self.window_size)
        self.image_resize = tf.keras.layers.Dense(self.hidden_size)
        self.transformer = TransformerBlock(emb_sz = self.hidden_size, multiheaded = True)
        self.vocab = tf.keras.layers.Dense(self.vocab_size)

        

    def call(self, encoded_images, captions):
        """
        :param encoded_images: tensor of shape [BATCH_SIZE x 2048]
        :param captions: tensor of shape [BATCH_SIZE x WINDOW_SIZE]
        :return: batch logits of shape [BATCH_SIZE x WINDOW_SIZE x VOCAB_SIZE]
        """
        # TODO
        images = self.image_resize(encoded_images)
        images = tf.expand_dims(images, axis = 1)
        embeddings = self.pos_encoding(captions)
        transformer = self.transformer(embeddings, images)
        logits = self.vocab(transformer)
        return logits

    def get_config(self):
        base_config = super().get_config()
        config = {k:getattr(self, k) for k in ["vocab_size", "hidden_size", "window_size"]}
        return {**base_config, **config}

    @classmethod
    def from_config(cls, config):
        return cls(**config)    
