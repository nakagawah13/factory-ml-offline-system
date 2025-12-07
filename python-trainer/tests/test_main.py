"""Tests for trainer.main module.

This module provides tests for the main training pipeline entry point.
"""

from unittest.mock import patch, mock_open

import pytest

from trainer.main import load_config, load_schema


class TestLoadConfig:
    """Tests for load_config function."""
    
    def test_load_config_success(self):
        """Test loading a valid JSON config file."""
        config_data = '{"training": {"test_size": 0.2}}'
        
        with patch("builtins.open", mock_open(read_data=config_data)):
            result = load_config("config.json")
        
        assert result == {"training": {"test_size": 0.2}}
    
    def test_load_config_file_not_found(self):
        """Test FileNotFoundError when config file doesn't exist."""
        with patch("builtins.open", side_effect=FileNotFoundError):
            with pytest.raises(FileNotFoundError):
                load_config("nonexistent.json")
    
    def test_load_config_invalid_json(self):
        """Test ValueError when config file has invalid JSON."""
        invalid_json = '{"training": invalid}'
        
        with patch("builtins.open", mock_open(read_data=invalid_json)):
            with pytest.raises(ValueError):
                load_config("config.json")


class TestLoadSchema:
    """Tests for load_schema function."""
    
    def test_load_schema_success(self):
        """Test loading a valid JSON schema file."""
        schema_data = '{"columns": [{"name": "value", "type": "float"}]}'
        
        with patch("builtins.open", mock_open(read_data=schema_data)):
            result = load_schema("schema.json")
        
        assert result == {"columns": [{"name": "value", "type": "float"}]}
    
    def test_load_schema_file_not_found(self):
        """Test FileNotFoundError when schema file doesn't exist."""
        with patch("builtins.open", side_effect=FileNotFoundError):
            with pytest.raises(FileNotFoundError):
                load_schema("nonexistent.json")
    
    def test_load_schema_invalid_json(self):
        """Test ValueError when schema file has invalid JSON."""
        invalid_json = '{"columns": invalid}'
        
        with patch("builtins.open", mock_open(read_data=invalid_json)):
            with pytest.raises(ValueError):
                load_schema("schema.json")
