CREATE TABLE played_matches (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    player_name VARCHAR(40) NOT NULL,
    opponent_name VARCHAR(40) NOT NULL,
    winner VARCHAR(40) NOT NULL,
    turns INTEGER NOT NULL,
    player_health INTEGER NOT NULL,
    opponent_health INTEGER NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_played_matches_created_at ON played_matches (created_at DESC);

CREATE TABLE simulated_matches (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    first_name VARCHAR(40) NOT NULL,
    second_name VARCHAR(40) NOT NULL,
    result_json TEXT NOT NULL
);

CREATE INDEX idx_simulated_matches_created_at ON simulated_matches (created_at DESC);
