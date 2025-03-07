# INSERT INTO users (nickname, role, oauth_provider, social_id, introduction, created_at, modified_at)
# SELECT 'Test User1', 'USER', 'GOOGLE', 'google-test1', '안녕하세요, Test User1입니다.', NOW(), NOW()
# WHERE NOT EXISTS (SELECT 1 FROM users);
#
# INSERT INTO users (nickname, role, oauth_provider, social_id, introduction, created_at, modified_at)
# SELECT 'Test User2', 'USER', 'KAKAO', 'kakao-test2', '반갑습니다. Test User2입니다.', NOW(), NOW()
# WHERE NOT EXISTS (SELECT 1 FROM users);
#
# INSERT INTO users (nickname, role, oauth_provider, social_id, introduction, created_at, modified_at)
# SELECT 'Test User3', 'USER', 'GOOGLE', 'google-test3', 'Test User3입니다.', NOW(), NOW()
# WHERE NOT EXISTS (SELECT 1 FROM users);